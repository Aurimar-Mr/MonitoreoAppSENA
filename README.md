Estructura de Paquetes en app/src/main/java
com.tuproyecto.bmis.ui

.auth: Este paquete es fundamental porque las pantallas de autenticación son compartidas por ambos roles.
      -LoginActivity.kt y activity_login.xml: Para el inicio de sesión de usuarios y administradores.
      -ForgotPasswordActivity.kt y activity_forgot_password.xml: Para el proceso de recuperación de contraseña, también compartido.
      -SignupActivity.kt y activity_signup.xml: Solo para el registro de usuarios regulares, ya que los administradores probablemente se crean de forma manual.

.user: Este es el paquete exclusivo para las pantallas que solo el usuario regular puede ver.
      -HomeUserActivity.kt y activity_home_user.xml: La pantalla de bienvenida del usuario.
      -SensorDataActivity.kt y activity_sensor_data.xml: Para visualizar las tablas de los sensores.
      -AlertsActivity.kt y activity_alerts.xml: Para recibir las alertas.

.admin: Este paquete contiene las pantallas específicas para el administrador.
      -HomeAdminActivity.kt y activity_home_admin.xml: La pantalla de bienvenida del administrador.
      -AdminDashboardActivity.kt y activity_admin_dashboard.xml: Para los gráficos y el menú de usuarios.
      -UserManagementActivity.kt y activity_user_management.xml: Si hay una pantalla dedicada para gestionar usuarios.

.common: Puedes crear este paquete para las Activities o Fragments que son compartidos en los menús laterales o en otras partes de la app.
    -SidebarMenuFragment.kt: Un Fragmento que puede ser reutilizado en las pantallas principales tanto de usuario como de administrador, solo mostrando u ocultando ciertos elementos según el rol.
