from rest_framework import generics
from UserApp.models.Model_Cliente import Cliente
from UserApp.serializers.clienteSerializer import ClienteSerializer

class ClienteDetailView(generics.RetrieveAPIView):
    queryset = Cliente.objects.all()
    serializer_class = ClienteSerializer