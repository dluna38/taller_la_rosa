from django.http.response import Http404
from rest_framework import status,generics,exceptions
from rest_framework.views import APIView
from rest_framework.response import Response
from ..models.Model_Vehiculo import Vehiculo
from ..serializers import VehiculoSerializer
from django.shortcuts import get_object_or_404
import re
from django.views.decorators.http import require_http_methods
from django.http import HttpResponse

ERROR_500 = Response(data={"msg":"Un error ocurrio"},status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class VehiculoView (APIView):
    def post(self,request,*args,**kwargs):
        #probar si enviando en minuscula es igual a minuscula en el serializer
        try:
            #mover a el serializador}            
            if (utilIsValidPlaca(request.data["placa"])):

                request.data["placa"] = request.data["placa"].upper()
                request.data["combustible"] = request.data["combustible"].upper()
                request.data["propietario"] = request.data["propietario"].upper()
                
                vehiculoSerial = VehiculoSerializer(data=request.data)
                vehiculoSerial.is_valid(raise_exception=True)
                vehiculoSerial.save()
                
                return Response(status=status.HTTP_201_CREATED)
            else:
                return Response(data={"msg":"Error con la placa"},status=status.HTTP_400_BAD_REQUEST)
        except KeyError as e:
            ERROR_500.data["debug"] = "Error leyendo el JSON"
            return ERROR_500
        except exceptions.ValidationError as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500
    
    def get(self,request,*args,**kwargs):
        try:
            vehiculos = VehiculoSerializer(Vehiculo.objects.all(),many=True)
            return Response(vehiculos.data,status=status.HTTP_200_OK)
        except:
            return ERROR_500

    def put(self,request,*args,**kwargs):
        try:
            #mover a el serializador}            
            if (utilIsValidPlaca(request.data["placa"])):
                Vehiculo.objects.get(placa=request.data["placa"])
                request.data["placa"] = request.data["placa"].upper()
                request.data["combustible"] = request.data["combustible"].upper()

                vehiculoSerial = VehiculoSerializer(data=request.data)
                vehiculoSerial.is_valid(raise_exception=True)
                vehiculoSerial.save()
                
                return Response(status=status.HTTP_201_CREATED)
            else:
                return Response(data={"msg":"Error con la placa"},status=status.HTTP_400_BAD_REQUEST)
        except KeyError as e:
            ERROR_500.data["debug"] = "Error leyendo el JSON"
            return ERROR_500
        except Vehiculo.DoesNotExist as e:
            ERROR_500.data["debug"] = "No existe el vehiculo"
            return ERROR_500
        except :
            return ERROR_500

    def delete(self,request,*args,**kwargs):
        try:
            vehiculo = get_object_or_404(Vehiculo,placa=kwargs["placa"].upper())
            vehiculo.delete()
            return Response(status=status.HTTP_200_OK)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except:
            return ERROR_500



def utilIsValidPlaca(placa): 
    return len(placa) == 6 and re.match("[A-Z]{3}[0-9]{3}",placa.upper())

import json
@require_http_methods(["GET"])
def getVehiculoById(request,placa):

    try:
        vehiculo = Vehiculo.objects.get(placa=placa.upper())        
        return HttpResponse(content=json.dumps(vehiculo.transformToDic()),status=status.HTTP_200_OK,content_type="application/json")
    except Vehiculo.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except :
        return HttpResponse(status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class getVehiculosByPropietario(APIView):
    def get(self,request,*args,**kwargs):

        try:
            vehiculos = VehiculoSerializer(Vehiculo.objects.filter(propietario=kwargs["documento"].upper()),many=True)   
            return Response(vehiculos.data,status=status.HTTP_200_OK)
        except Vehiculo.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except :
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)



   
    

