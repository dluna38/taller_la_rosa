from rest_framework import status, views
from rest_framework.response import Response
from UserApp.serializers.revisionSerializer import RevisionSerializer
from ..models.revision import Revision
from django.shortcuts import get_object_or_404
from django.http.response import Http404

ERROR_500 = Response(data={"msg":"Un error ocurrio"},status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class RevisionView(views.APIView):
    def post(self, request, *args, **kwargs):
        try:
            revisionSerializer = RevisionSerializer(data=request.data)
            revisionSerializer.is_valid(raise_exception=True)
            revisionSerializer.save()
            return Response(status=status.HTTP_201_CREATED)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500

    def get(self, request, *args, **kwargs):
        try:
            revisiones =  RevisionSerializer(Revision.objects.all(),many=True)
            return Response(revisiones.data,status=status.HTTP_200_OK)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500

    def put(self, request, *args, **kwargs):
        #verificaciones por hacer
        try:
            revision = get_object_or_404(Revision,idAgendamiento=request.data["idAgendamiento"])
            revisionSerializer = RevisionSerializer(revision,data=request.data)
            revisionSerializer.is_valid(raise_exception=True)
            revisionSerializer.save()
            return  Response(status=status.HTTP_202_ACCEPTED)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500
       
    def delete(self, request, *args, **kwargs):
        try:
            revision = get_object_or_404(Revision,idAgendamiento=kwargs["idAgendamiento"])
            revision.delete()
            return Response(status=status.HTTP_200_OK)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500


class RevisionByIdAgendamiento(views.APIView):
    def get(self, request, *args, **kwargs):
        try:
            revision = RevisionSerializer(get_object_or_404(Revision,idAgendamiento=kwargs["idAgendamiento"]))  

            return Response(revision.data,status=status.HTTP_200_OK)
        except Http404:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500

class RevisionesByVehiculo(views.APIView):
    def get(self, request, *args, **kwargs):
        try:
            revision = RevisionSerializer(Revision.objects.filter(idVehiculo = kwargs["placa"]),many=True)  
            return Response(revision.data,status=status.HTTP_200_OK)
        except Revision.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            ERROR_500.data["debug"] = e.args
            return ERROR_500