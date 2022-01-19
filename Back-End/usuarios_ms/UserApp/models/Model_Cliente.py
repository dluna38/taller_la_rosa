from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin, BaseUserManager
from django.contrib.auth.hashers import make_password

class UserManager(BaseUserManager):
    def create_user(self, documento, password=None):
        """
        Creates and saves a user with the given username and password.
        """
        if not documento :
            raise ValueError('Users must have an Documento')
        user = self.model(documento=documento)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, documento, password):
        """
        Creates and saves a superuser with the given username and password.
        """
        user = self.create_user(
            documento=documento,
            password=password,
            )
        user.is_admin = True
        user.save(using=self._db)
        return user

#Creando el modelo cliente
class Cliente(AbstractBaseUser,PermissionsMixin):
    documento = models.CharField(primary_key=True,max_length=12)
    nombre = models.CharField(max_length=50)
    apellido = models.CharField(max_length=50)
    telefono = models.CharField(max_length=15)
    #cambiar por EmailField
    correo = models.CharField(max_length=30)
    password = models.CharField(max_length=24)
    is_superuser = models.BooleanField(default=False)

    def save(self, **kwargs):
        some_salt = 'mMUj0DrIK6vgtdIYepkIxN'
        self.password = make_password(self.password, some_salt)
        super().save(**kwargs)

    def __str__(self) -> str:
        return "documento:"+self.documento+",correo:"+self.correo

    def transformToDic(self) -> dict:
        return {
            "documento":self.documento,
            "nombre":self.nombre,
            "apellido":self.apellido,
            "telefono":self.telefono,
            "correo":self.correo,
        }
    objects = UserManager()
    USERNAME_FIELD = 'documento'