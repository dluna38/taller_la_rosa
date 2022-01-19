from rest_framework import serializers
from UserApp.models.Model_Cliente import Cliente
class ClienteSerializer(serializers.ModelSerializer):
	class Meta:
		model = Cliente
		fields = ['documento','password', 'nombre', 'apellido', 'telefono', 'correo','is_superuser']
	
	def create(self, validated_data):
		userInstance = Cliente.objects.create(**validated_data)
		return userInstance
	
	def to_representation(self, obj):
		cliente = Cliente.objects.get(documento=obj.documento)      
		return {
                    'documento': cliente.documento, 
                    'nombre': cliente.nombre,
                    'apellido': cliente.apellido,
                    'telefono': cliente.telefono,
					'correo':cliente.correo
                }
