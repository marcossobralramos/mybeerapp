import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/pages/main_ui.dart';

void main() => runApp(MyApp());

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
