from django.contrib import admin
from .models.Model_Cliente import Cliente
from .models.Model_Vehiculo import Vehiculo

# Register your models here.
admin.site.register(Cliente)
admin.site.register(Vehiculo)