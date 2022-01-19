from rest_framework import status, views
from rest_framework.response import Response
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer
from UserApp.serializers.clienteSerializer import ClienteSerializer

class ClienteCreateView(views.APIView):
	def post(self, request, *args, **kwargs):
		serializer = ClienteSerializer(data=request.data)
		serializer.is_valid(raise_exception=True)
		serializer.save()
		tokenData = {"documento":request.data["documento"],"password":request.data["password"]}
		tokenSerializer = TokenObtainPairSerializer(data=tokenData)
		tokenSerializer.is_valid(raise_exception=True)

		return Response(tokenSerializer.validated_data, status=status.HTTP_201_CREATED)