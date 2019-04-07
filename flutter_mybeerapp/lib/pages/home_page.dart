import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/pages/cesta_pages.dart';
import 'package:flutter_mybeerapp/src/app.dart';

import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';

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
        appBar: AppBar(
          elevation: 2.0,
          backgroundColor: Colors.teal,
          title: Text("My Beer, My Cash",
              style: TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.w600,
                  fontSize: 20.0)),
        ),
        body: StaggeredGridView.count(
          crossAxisCount: 2,
          crossAxisSpacing: 12.0,
          mainAxisSpacing: 12.0,
          padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 200.0),
          children: <Widget>[
            MainUI.buildTile(
                Padding(
                  padding: const EdgeInsets.all(24.0),
                  child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: <Widget>[
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            Text('Clique aqui e vamos às compras',
                                style: TextStyle(color: Colors.blueAccent)),
                            Text('Minhas cestas',
                                style: TextStyle(
                                    color: Colors.black,
                                    fontWeight: FontWeight.w700,
                                    fontSize: 20.0)),
                          ],
                        ),
                        Material(
                            color: Colors.teal,
                            borderRadius: BorderRadius.circular(24.0),
                            child: Center(
                                child: Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Icon(Icons.add_shopping_cart,
                                  color: Colors.white, size: 30.0),
                            )))
                      ]),
                ),
                onTap: () => {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => CestaPage()),
                      )
                    }),
          ],
          staggeredTiles: [
            StaggeredTile.extent(2, 110.0),
          ],
        ),
        drawer: MainUI.getDrawer(context));
  }
}
