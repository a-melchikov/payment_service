from django.urls import path
from .views import (
    CartListView,
    AddToCartView,
    CheckoutView,
    RemoveFromCartView,
    UpdateCartView,
)

urlpatterns = [
    path("", CartListView.as_view(), name="cart-list"),
    path("add/<int:pk>/", AddToCartView.as_view(), name="add-to-cart"),
    path("remove/<int:pk>/", RemoveFromCartView.as_view(), name="remove-from-cart"),
    path("checkout/", CheckoutView.as_view(), name="checkout"),
    path("update/<int:item_id>/", UpdateCartView.as_view(), name="update-cart"),
]
