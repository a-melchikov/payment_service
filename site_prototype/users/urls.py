from django.urls import path
from django.contrib.auth.views import (
    PasswordChangeDoneView,
    LogoutView,
)

from .views import (
    CustomPasswordChangeView,
    UserProfileView,
    UserRegisterView,
    UserLoginView,
)

urlpatterns = [
    path("register/", UserRegisterView.as_view(), name="register"),
    path("login/", UserLoginView.as_view(), name="login"),
    path("logout/", LogoutView.as_view(), name="logout"),
    path("profile/", UserProfileView.as_view(), name="profile"),
    path(
        "password-change/",
        CustomPasswordChangeView.as_view(template_name="users/password_change.html"),
        name="password_change",
    ),
    path(
        "password-change/done/",
        PasswordChangeDoneView.as_view(template_name="users/password_change_done.html"),
        name="password_change_done",
    ),
]
