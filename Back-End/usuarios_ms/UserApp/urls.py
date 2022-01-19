from django.urls import path
from .views import vehiculoViews,clienteViews,revisionViews
from .views import ClienteCreateView
from .views import ClienteDetailView

app_name="UserApp"
#del proyecto -> path('cliente/',include('UserApp.urls'))
urlpatterns = [
    path('', clienteViews.ClienteView.as_view()), #post(body), get
    path('delete/<str:documento>/', clienteViews.ClienteView.as_view()), #delete(/documento)
    path('detalle/<str:documento>/',clienteViews.ClienteByDocumento.as_view()), #get : un solo cliente
    path('super/',clienteViews.CreateSuperUser.as_view()),
    path('vehiculos/',vehiculoViews.VehiculoView.as_view()), #post(body),update(body),get 
    path('vehiculos/<str:placa>',vehiculoViews.VehiculoView.as_view()), #para delete
    path('vehiculo/<str:placa>',vehiculoViews.getVehiculoById),
    path('vehiculos/prop/<str:documento>',vehiculoViews.getVehiculosByPropietario.as_view()),
    path('buscar/',clienteViews.SearchByCliente.as_view()),

    path('vehiculos/revisiones/',revisionViews.RevisionView.as_view()), # crud
    path('vehiculos/revisiones/<str:idAgendamiento>',revisionViews.RevisionView.as_view()),
    path('vehiculos/revisiones/a/<str:idAgendamiento>',revisionViews.RevisionByIdAgendamiento.as_view()),
    path('vehiculos/revisiones/v/<str:placa>',revisionViews.RevisionesByVehiculo.as_view()),
]