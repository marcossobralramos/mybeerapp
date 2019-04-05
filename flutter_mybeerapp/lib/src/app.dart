import 'package:flutter/material.dart';
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
  static getDrawer(context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            child: null,
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
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => ModeloPage()),
              );
            },
          ),
          ListTile(
            title: Text('Lojas'),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => LojaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Bebidas'),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => BebidaPage()),
              );
            },
          ),
          ListTile(
            title: Text('Produtos'),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => ProdutoPage()),
              );
            },
          ),
          ListTile(
            title: Text('Cestas'),
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
        body: Center(child: Text('Seja bem-vindo ao My Beer, My Cash! '
            'Nós vamos te ajudar a montar suas cestas de bebidas mais em conta :D')),
        drawer: MainUI.getDrawer(context));
  }
}