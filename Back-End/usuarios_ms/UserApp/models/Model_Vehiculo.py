from django.db import models
from .Model_Cliente import Cliente

class Vehiculo(models.Model):

    placa = models.CharField(primary_key=True,max_length=6)
    propietario = models.ForeignKey(Cliente, related_name='propietario', on_delete=models.CASCADE)
    marca = models.CharField(max_length=50)
    modelo = models.CharField(max_length=50)
    tipo = models.CharField(max_length=50)
    cilindraje = models.CharField(max_length=50)
    combustible = models.CharField(max_length=50)

    def __str__(self) -> str:
        return self.placa+"propietario:"+str(self.propietario)

    def transformToDic(self,onlyVehiculo =False):
        if onlyVehiculo:
            propietario = self.propietario.documento
        else: propietario  =self.propietario.transformToDic()
        dic = {
            "placa":self.placa,
            "propietario": propietario,
            "marca":self.marca,
            "modelo":self.modelo,
            "tipo":self.tipo,
            "cilindraje":self.cilindraje,
            "combustible":self.combustible,
        }
        return  dic
