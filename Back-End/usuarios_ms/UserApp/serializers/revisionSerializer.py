from rest_framework import serializers
from ..models.revision import Revision


class RevisionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Revision
        fields = '__all__'
