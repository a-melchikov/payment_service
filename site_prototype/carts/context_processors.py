from .models import Cart

def cart_item_count(request):
    if request.user.is_authenticated:
        cart_items = Cart.objects.filter(user=request.user)
        item_count = sum(cart_item.quantity for cart_item in cart_items)
        return {'cart_item_count': item_count}
    return {'cart_item_count': 0}
