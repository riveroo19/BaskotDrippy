# BaskotDrippy
Proyecto DWS

## PARTICIPANTES ##

* JAVIER RIVERO MAYORGA
* JAVIER VALSERA CASTRO
* IVÁN MÁRQUEZ LÓPEZ

# FUNCIONAMIENTO BÁSICO FINAL

1. PENDIENTE DE HACER: INICIO DE SESIÓN CON RRSS, CAMBIAR DE VERSIÓN A SPRING 3.0.
2. Debes configurar el application.properties si es la primera vez que inicias la aplicación: cambiar el ddl-auto a create si es la primera vez que la inicias,la segunda vez lo cambias a update.
3. A su vez, cambiar el usuario y contraseña para la instancia en local de mysql. Además tendrás que crear un esquema nuevo al principio de todo llamado dwsF2.
4. Finalmente, la segunda vez que se ejecute, antes de ello deberías descomentar el constructor @PostConstruct de la clase GarmentController.
5. Una vez hecho esto, en principio debería ir todo correcto.
6. Hay 3 usuarios predefinidos, vienen en el constructor de la clase UserController, mas el usuario administrador (admin, secure!pass).
7. Un usuario puede: crear prendas, crear outfits para sí mismo, añadir prendas a su outfit, eliminarlas de su outfit, eliminar o modificar sus credenciales y/o sus outfits. Podrá acceder a todas las páginas que no tengan que ver con alterar objetos que no son de su propiedad, además de no poder a acceder a la página de administrador. Finalmente no podrá consultar el usuario del administrador, ni los objetos que le pertenezcan a este.
8. Un usuario no registrado podrá ver los outfits, las prendas y navegar por algunas páginas públicas de la web.
9. Un usuario administrador podrá acceder a todo lo anterior, "violando" cualquier usuario y sus objetos, así como borrar todo desde su página de administración.
10. Desde la api rest se podrá hacer todo esto mencionado anteriormente, teniendo en cuenta que las respuestas serán objetos en json que representarán la información consultada/modificada/eliminada, según la petición ejecutada.
