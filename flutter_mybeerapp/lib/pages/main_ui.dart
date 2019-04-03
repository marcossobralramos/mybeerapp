import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/pages/marca_pages.dart';

class MainUI {
  static getDrawer(context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            decoration: BoxDecoration(
              color: Colors.teal,
            ),
          ),
          ListTile(
            title: Text('Marcas'),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => MarcaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Modelos'),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            title: Text('Lojas'),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            title: Text('Bebidas'),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            title: Text('Produtos'),
            onTap: () {
              Navigator.pop(context);
            },
          ),
          ListTile(
            title: Text('Cestas'),
            onTap: () {
              Navigator.pop(context);
            },
          ),
        ],
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  MyHomePageState createState() => MyHomePageState();
}

class MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text(widget.title)),
        body: Center(child: Text('Seja bem-vindo!')),
        drawer: MainUI.getDrawer(context));
  }
}
