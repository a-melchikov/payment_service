from django.shortcuts import get_object_or_404
from django.urls import reverse_lazy
from django.views.generic import ListView, CreateView, DeleteView
from django.contrib.auth.mixins import LoginRequiredMixin

from products.models import Product
from .models import Cart


class CartListView(LoginRequiredMixin, ListView):
    model = Cart
    template_name = "carts/cart_list.html"
    context_object_name = "cart_items"

    def get_queryset(self):
        return Cart.objects.filter(user=self.request.user)


class AddToCartView(LoginRequiredMixin, CreateView):
    model = Cart
    fields = ["product", "quantity"]
    success_url = reverse_lazy("cart-list")

    def form_valid(self, form):
        form.instance.user = self.request.user
        product = get_object_or_404(Product, pk=self.kwargs["pk"])
        form.instance.product = product
        return super().form_valid(form)


class RemoveFromCartView(LoginRequiredMixin, DeleteView):
    model = Cart
    success_url = reverse_lazy("cart-list")

    def get_queryset(self):
        return Cart.objects.filter(user=self.request.user)
