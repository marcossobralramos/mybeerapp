import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/pages/home_page.dart';
import 'package:toast/toast.dart';

import 'package:flutter_mybeerapp/pages/bebida_pages.dart';
import 'package:flutter_mybeerapp/pages/cesta_pages.dart';
import 'package:flutter_mybeerapp/pages/loja_pages.dart';
import 'package:flutter_mybeerapp/pages/marca_pages.dart';
import 'package:flutter_mybeerapp/pages/modelo_pages.dart';
import 'package:flutter_mybeerapp/pages/produto_pages.dart';

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'My Beer, My Cash',
      theme: ThemeData(
        primarySwatch: Colors.teal,
      ),
      home: MyHomePage(title: 'My Beer, My Cash'),
    );
  }
}

class MainUI {
  static Widget buildTile(Widget child, {Function() onTap}) {
    return Material(
        elevation: 14.0,
        borderRadius: BorderRadius.circular(12.0),
        shadowColor: Color(0x802196F3),
        child: InkWell(
          // Do onTap() if it isn't null, otherwise do print()
            onTap: onTap != null
                ? () => onTap()
                : () {
            },
            child: child));
  }
  static getDrawer(context) {
    return Drawer(
      child: new ListView(
        padding: const EdgeInsets.only(top: 0.0),
        children: <Widget>[
          new UserAccountsDrawerHeader(
              accountName: const Text("Seja bem-vindo ao My Beer, My Cash!"),
              currentAccountPicture: new CircleAvatar(
                  backgroundColor: Colors.brown,
                  child: new Text("Olá!")
              ),
              otherAccountsPictures: <Widget>[
                new GestureDetector(
                    onTap: () => {},
                    child: new Icon(Icons.settings)
                )
              ]
          ),
          new ListTile(
            leading: new Icon(Icons.apps),
            title: new Text('Dashboard'),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => MyHomePage()),
              );
            },
          ),
          new Divider(),
          ListTile(
            title: Text('Marcas'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => MarcaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Modelos'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => ModeloPage()),
              );
            },
          ),
          ListTile(
            title: Text('Lojas'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => LojaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Bebidas'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => BebidaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Produtos'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => ProdutoPage()),
              );
            },
          ),
          ListTile(
            title: Text('Cestas'),
            trailing: Icon(Icons.arrow_forward),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => CestaPage()),
              );
            },
          ),
        ],
      ),
    );
  }
}