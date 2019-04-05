import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:flutter_mybeerapp/models/cesta.dart';
import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:flutter_mybeerapp/models/produto.dart';
import 'package:flutter_mybeerapp/repositories/cesta_repository.dart';
import 'package:flutter_mybeerapp/src/app.dart';
import 'package:flutter_mybeerapp/repositories/bebida_repository.dart';
import 'package:flutter_mybeerapp/repositories/produto_repository.dart';
import 'package:flutter_mybeerapp/repositories/loja_repository.dart';
import 'package:toast/toast.dart';

class CestaPage extends StatefulWidget {
  @override
  CestaPageState createState() => CestaPageState();
}

class CestaPageState extends State<CestaPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Cestas")),
      body: Center(
          child: FutureBuilder(
        future: CestaRepository.retrieveAll(),
        builder: (BuildContext context, AsyncSnapshot snapshot) {
          switch (snapshot.connectionState) {
            case ConnectionState.none:
            case ConnectionState.waiting:
              return new CircularProgressIndicator();
            default:
              if (snapshot.hasError)
                return new Text('Error: ${snapshot.error}');
              else
                return _createListView(context, snapshot);
          }
        },
      )),
      drawer: MainUI.getDrawer(context),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) =>
                    CestaForm(cesta: null, title: "Nova Cesta")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Cesta> cestas = snapshot.data;
    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: cestas.length,
      itemBuilder: (BuildContext context, int index) {
        return new Card(
          elevation: 8.0,
          margin: new EdgeInsets.symmetric(horizontal: 10.0, vertical: 6.0),
          child: Container(
            decoration: BoxDecoration(color: Colors.teal),
            child: ListTile(
                contentPadding:
                    EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
                leading: Container(
                    padding: EdgeInsets.only(right: 12.0),
                    decoration: new BoxDecoration(
                        border: new Border(
                            right: new BorderSide(
                                width: 1.0, color: Colors.white24))),
                    child: GestureDetector(
                      child: Icon(Icons.delete, color: Colors.white),
                      onTap: () async {
                        bool success = (await CestaRepository.remove(
                            cestas[index].id));
                        if (success) {
                          Toast.show("Cesta deletada com sucesso!", context,
                              duration: Toast.LENGTH_LONG);
                          setState(() {});
                        } else {
                          Toast.show(
                              "Erro ao deletar. Tente novamente!", context,
                              duration: Toast.LENGTH_LONG);
                        }
                      },
                    )),
                title: Text(
                  cestas[index].toString(),
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                subtitle: Row(
                  children: <Widget>[
                    Text(
                        cestas[index].litros.toStringAsFixed(3) + "L"
                            " | R\$" +
                            cestas[index].total.toStringAsFixed(2),
                        style: TextStyle(color: Colors.white))
                  ],
                ),
                trailing: GestureDetector(
                  child: Icon(Icons.keyboard_arrow_right,
                      color: Colors.white, size: 30.0),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => CestaForm(
                              cesta: cestas[index],
                              title: "Editando Cesta: " +
                                  cestas[index].toString())),
                    );
                  },
                )),
          ),
        );
      },
    );
  }
}

class CestaForm extends StatefulWidget {
  final Cesta cesta;
  final String title;

  const CestaForm({Key key, this.cesta, this.title}) : super(key: key);

  @override
  CestaFormState createState() => CestaFormState(cesta, title);
}

class CestaFormState extends State<CestaForm> {
  final Cesta cesta;
  final String title;

  final descricaoController = TextEditingController();

  CestaFormState(this.cesta, this.title);

  @override
  void initState() {
    if (cesta != null) descricaoController.text = cesta.descricao;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(title: Text(this.title)),
      body: new Container(
          padding: const EdgeInsets.all(15.0),
          color: Colors.white,
          child: new Container(
              color: Colors.white,
              child: new Container(
                child: new Center(
                    child: new Column(
                  children: <Widget>[
                    new Padding(padding: EdgeInsets.only(top: 30.0)),
                    new TextField(
                      decoration: new InputDecoration(
                        labelText: "Informe a descrição da cesta",
                        fillColor: Colors.white,
                        border: new OutlineInputBorder(
                          borderRadius: new BorderRadius.circular(25.0),
                          borderSide: new BorderSide(),
                        ),
                        //fillColor: Colors.green
                      ),
                      keyboardType: TextInputType.number,
                      style: new TextStyle(
                        fontFamily: "Poppins",
                      ),
                      controller: descricaoController,
                    ),
                  ],
                )),
              ))),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<Cesta> futureCesta;
          if (this.cesta == null) {
            Cesta cesta = new Cesta(
                id: 0,
                descricao: descricaoController.text);
            futureCesta = CestaRepository.create(cesta);
          } else {
            Cesta bebida = new Cesta(
                id: this.cesta.id,
                descricao: descricaoController.text);
            futureCesta = CestaRepository.update(bebida);
          }

          futureCesta.then((Cesta value) {
            if (value != null) {
              Navigator.pop(context);
              Toast.show("Dados salvos com sucesso!", context,
                  duration: Toast.LENGTH_LONG);
            }
          }, onError: (e) {
            setState(() {
              Toast.show(e.toString(), context, duration: Toast.LENGTH_LONG);
            });
          });
        },
        label: Text("Salvar"),
        icon: Icon(Icons.save),
      ),
    );
  }
}
