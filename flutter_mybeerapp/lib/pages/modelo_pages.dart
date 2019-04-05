import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/modelo.dart';
import 'package:flutter_mybeerapp/src/app.dart';
import 'package:flutter_mybeerapp/repositories/modelo_repository.dart';
import 'package:toast/toast.dart';

class ModeloPage extends StatefulWidget {
  @override
  ModeloPageState createState() => ModeloPageState();
}

class ModeloPageState extends State<ModeloPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Modelos")),
      body: Center(
          child: FutureBuilder(
        future: ModeloRepository.retrieveAll(),
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
                    ModeloForm(modelo: null, title: "Novo Modelo")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Modelo> modelos = snapshot.data;
    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: modelos.length,
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
                            (await ModeloRepository.remove(modelos[index].id));
                        if (success) {
                          Toast.show("Modelo deletada com sucesso!", context,
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
                  modelos[index].toString(),
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                subtitle: Row(
                  children: <Widget>[
                    Icon(Icons.linear_scale, color: Colors.yellowAccent),
                    Text(" ID: " + modelos[index].id.toString(),
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
                          builder: (context) => ModeloForm(
                              modelo: modelos[index],
                              title: "Editando Modelo: " +
                                  modelos[index].toString())),
                    );
                  },
                )),
          ),
        );
      },
    );
  }
}

class ModeloForm extends StatefulWidget {
  final Modelo modelo;
  final String title;

  const ModeloForm({Key key, this.modelo, this.title}) : super(key: key);

  @override
  ModeloFormState createState() => ModeloFormState(modelo, title);
}

class ModeloFormState extends State<ModeloForm> {
  final Modelo modelo;
  final String title;

  final nomeController = TextEditingController();
  final volumeController = TextEditingController();

  ModeloFormState(this.modelo, this.title);

  @override
  Widget build(BuildContext context) {
    if (modelo != null) {
      nomeController.text = modelo.nome;
      volumeController.text = modelo.volume.toString();
    }
    return new Scaffold(
      appBar: AppBar(title: Text(this.title)),
      body: new Container(
          padding: const EdgeInsets.all(30.0),
          color: Colors.white,
          child: new Container(
            child: new Center(
                child: new Column(children: [
              new Padding(padding: EdgeInsets.only(top: 20.0)),
              new Text(
                'Novo Modelo',
                style: new TextStyle(color: Colors.teal, fontSize: 25.0),
              ),
              new Padding(padding: EdgeInsets.only(top: 30.0)),
              new TextField(
                autofocus: true,
                decoration: new InputDecoration(
                  labelText: "Informe o nome do modelo",
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
              new Padding(padding: EdgeInsets.only(top: 15.0)),
              new TextField(
                decoration: new InputDecoration(
                  labelText: "Informe o volume",
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
                controller: volumeController,
              ),
            ])),
          )),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<Modelo> futureModelo;
          if (this.modelo == null) {
            Modelo modelo = new Modelo(
                id: 0,
                nome: nomeController.text,
                volume: int.parse(volumeController.text));
            futureModelo = ModeloRepository.create(modelo);
          } else {
            Modelo marca = new Modelo(
                id: this.modelo.id,
                nome: nomeController.text,
                volume: int.parse(volumeController.text));
            futureModelo = ModeloRepository.update(marca);
          }

          futureModelo.then((Modelo value) {
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
