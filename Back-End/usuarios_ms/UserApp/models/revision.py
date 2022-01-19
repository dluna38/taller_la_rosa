from django.db import models



class Revision(models.Model):
    id = models.AutoField(primary_key=True)
    idAgendamiento = models.CharField(null=False,max_length=32,unique=True)
    idVehiculo = models.ForeignKey('Vehiculo',on_delete=models.CASCADE)
    resultado = models.CharField(null=False,max_length=500)
    costoTotal = models.CharField(null=True,max_length=100)
    fechaRevision = models.DateTimeField(null=False,auto_now_add=True)
    fechaActualizacion = models.DateTimeField(null=False,auto_now=True)

    def __str__(self) -> str:
        return self.id+'|result= '+self.resultado