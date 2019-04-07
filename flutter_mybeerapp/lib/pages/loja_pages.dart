import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:flutter_mybeerapp/src/app.dart';
import 'package:flutter_mybeerapp/repositories/loja_repository.dart';
import 'package:toast/toast.dart';

class LojaPage extends StatefulWidget {
  @override
  LojaPageState createState() => LojaPageState();
}

class LojaPageState extends State<LojaPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        backgroundColor: Colors.transparent,
        leading: IconButton(
          color: Colors.teal,
          onPressed: () => Navigator.of(context).pop(),
          icon: Icon(Icons.arrow_back, color: Colors.teal),
        ),
        title: Text('Lojas',
            style: TextStyle(color: Colors.teal, fontWeight: FontWeight.w700)),
      ),
      body: Center(
          child: FutureBuilder(
            future: LojaRepository.retrieveAll(),
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
                    LojaForm(loja: null, title: "Nova Loja")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Loja> lojas = snapshot.data;

    if(lojas == null || lojas.length == 0)
      return new Text("Não há lojas cadastradas");

    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: lojas.length,
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
                        bool success =
                        (await LojaRepository.remove(lojas[index].id));
                        if (success) {
                          Toast.show("Loja deletada com sucesso!", context,
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
                  lojas[index].nome,
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                subtitle: Row(
                  children: <Widget>[
                    Icon(Icons.linear_scale, color: Colors.yellowAccent),
                    Text(" ID: " + lojas[index].id.toString(),
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
                          builder: (context) =>
                              LojaForm(
                                  loja: lojas[index],
                                  title: "Editando Loja: " +
                                      lojas[index].nome)),
                    );
                  },
                )),
          ),
        );
      },
    );
  }
}

class LojaForm extends StatefulWidget {
  final Loja loja;
  final String title;

  const LojaForm({Key key, this.loja, this.title}) : super(key: key);

  @override
  LojaFormState createState() => LojaFormState(loja, title);
}

class LojaFormState extends State<LojaForm> {
  final Loja loja;
  final String title;

  final nomeController = TextEditingController();

  LojaFormState(this.loja, this.title);

  @override
  Widget build(BuildContext context) {
    if (loja != null) nomeController.text = loja.nome;

    return new Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        backgroundColor: Colors.transparent,
        leading: IconButton(
          color: Colors.teal,
          onPressed: () => Navigator.of(context).pop(),
          icon: Icon(Icons.arrow_back, color: Colors.teal),
        ),
        title: Text(this.title,
            style: TextStyle(color: Colors.teal, fontWeight: FontWeight.w700)),
      ),
      body: new Container(
          padding: const EdgeInsets.all(30.0),
          color: Colors.white,
          child: new Container(
            child: new Center(
                child: new Column(children: [
                  new Padding(padding: EdgeInsets.only(top: 40.0)),
                  new Text(
                    'Nova Loja',
                    style: new TextStyle(color: Colors.teal, fontSize: 25.0),
                  ),
                  new Padding(padding: EdgeInsets.only(top: 30.0)),
                  new TextField(
                    decoration: new InputDecoration(
                      labelText: "Informe o nome da loja",
                      fillColor: Colors.white,
                      border: new OutlineInputBorder(
                        borderRadius: new BorderRadius.circular(25.0),
                        borderSide: new BorderSide(),
                      ),
                      //fillColor: Colors.green
                    ),
                    keyboardType: TextInputType.text,
                    style: new TextStyle(
                      fontFamily: "Poppins",
                    ),
                    controller: nomeController,
                  ),
                ])),
          )),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<Loja> futureLoja;
          if (this.loja == null) {
            Loja loja = new Loja(id: 0, nome: nomeController.text);
            futureLoja = LojaRepository.create(loja);
          } else {
            Loja loja = new Loja(
                id: this.loja.id, nome: nomeController.text);
            futureLoja = LojaRepository.update(loja);
          }

          futureLoja.then((Loja value) {
            if (value != null) {
              Navigator.pop(context);
              Toast.show("Dados salvos com sucesso!", context,
                  duration: Toast.LENGTH_LONG);
            }
          },
              onError: (e) {
                setState(() {
                  Toast.show(
                      e.toString(), context, duration: Toast.LENGTH_LONG);
                });
              });
        },
        label: Text("Salvar"),
        icon: Icon(Icons.save),
      ),
    );
  }
}