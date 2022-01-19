from rest_framework import status, views
from rest_framework.response import Response
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer
from UserApp.serializers.clienteSerializer import ClienteSerializer
from ..models.Model_Cliente import Cliente
from django.shortcuts import get_object_or_404
from django.http.response import Http404


ERROR_500 = Response(data={"msg":"Un error ocurrio"},status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class ClienteView (views.APIView):
    def post(self, request, *args, **kwargs):
        try:
            request.data["documento"] = request.data["documento"].upper();
            serializer = ClienteSerializer(data=request.data)
            serializer.is_valid(raise_exception=True)
            serializer.save()
            tokenData = {"documento":request.data["documento"],"password":request.data["password"]}
            tokenSerializer = TokenObtainPairSerializer(data=tokenData)
            tokenSerializer.is_valid(raise_exception=True)

            return Response(tokenSerializer.validated_data, status=status.HTTP_201_CREATED)
        except:
            return ERROR_500

    def get(self, request, *args, **kwargs):
        try:
            clientes =  ClienteSerializer(Cliente.objects.all(),many=True)
            return Response(clientes.data,status=status.HTTP_200_OK)
        except:
            return ERROR_500
    def put(self, request, *args, **kwargs):
        #verificaciones por hacer
        try:
            cliente = get_object_or_404(Cliente,documento=request.data["documento"].upper())

            cliente.telefono=request.data['telefono']
            cliente.nombre=request.data['nombre']
            cliente.apellido=request.data['apellido']
            cliente.correo=request.data['correo'] #sin serializer no se valida correo.

            cliente.save()            
            return  Response(status=status.HTTP_201_CREATED)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        # except:
        #     return ERROR_500
         

    def delete(self, request, *args, **kwargs):
        try:
            cliente = get_object_or_404(Cliente,documento=kwargs["documento"].upper())
            cliente.delete()
            return Response(status=status.HTTP_200_OK)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except:
            return ERROR_500


class ClienteByDocumento(views.APIView):
    def get(self,request,*args,**kwargs):
        try:
            cliente = ClienteSerializer(get_object_or_404(Cliente,documento=kwargs["documento"].upper()))   
            return Response(cliente.data,status=status.HTTP_200_OK)
        except Cliente.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500

class CreateSuperUser(views.APIView):
    def post(self,request,*args,**kwargs):
        try:
            request.data["documento"] = request.data["documento"].upper()
            request.data["is_superuser"] = True
            serializer = ClienteSerializer(data=request.data)
            serializer.is_valid(raise_exception=True)
            usuario = serializer.save()
            
            tokenData = {"documento":request.data["documento"],"password":request.data["password"]}
            tokenSerializer = TokenObtainPairSerializer(data=tokenData)
            tokenSerializer.is_valid(raise_exception=True)

            return Response(tokenSerializer.validated_data, status=status.HTTP_201_CREATED)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500

from django.db.models import Q
class SearchByCliente(views.APIView):
    def get(self,request,*args,**kwargs):
        #cadena: http://127.0.0.1:8000/cliente/buscar/?tipo=documento&argumento=CE
        try:
            dic = request.GET.dict()
            if (dic['tipo'] == 'documento'):
                resultado = Cliente.objects.filter(documento__contains=dic['argumento'])
                resultado = ClienteSerializer(resultado,many=True)
                return Response(resultado.data,status=status.HTTP_200_OK)             
            elif (dic['tipo'] == 'correo'):
                resultado = Cliente.objects.filter(correo__contains=dic['argumento'])
                resultado = ClienteSerializer(resultado,many=True)
                return Response(resultado.data,status=status.HTTP_200_OK)
            elif (dic['tipo'] == 'nombre'):
                resultado = Cliente.objects.filter(Q(nombre__contains=dic['argumento']) | Q(apellido__contains=dic['argumento']))
                resultado = ClienteSerializer(resultado,many=True)
                return Response(resultado.data,status=status.HTTP_200_OK) 
            else:
                return Response(data = {'msg':'el tipo no fue entendido'},status=status.HTTP_400_BAD_REQUEST)
        except KeyError as e:
            ERROR_500.data["debug"] = 'No se pudo encontrar: '+e.args[0]
            return ERROR_500  
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500 