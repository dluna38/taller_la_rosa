from rest_framework import serializers
from UserApp.models.Model_Vehiculo import Vehiculo
class VehiculoSerializer(serializers.ModelSerializer):
	class Meta:
		model = Vehiculo
		#fields = ['Placa','Marca', 'Modelo', 'Tipo', 'Cilindraje', 'Combustible']
		fields = '__all__'
