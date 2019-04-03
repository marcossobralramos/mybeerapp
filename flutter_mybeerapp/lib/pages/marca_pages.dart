import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/marca.dart';
import 'package:flutter_mybeerapp/pages/main_ui.dart';
import 'package:flutter_mybeerapp/repositories/marca_repository.dart';
import 'package:toast/toast.dart';

class MarcaPage extends StatefulWidget {
  @override
  MarcaPageState createState() => MarcaPageState();
}

class MarcaPageState extends State<MarcaPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Marcas")),
      body: Center(
        child: FutureBuilder(
        future: MarcaRepository.retrieveAll(),
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
                    MarcaForm(marca: null, title: "Nova Marca")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Marca> marcas = snapshot.data;
    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: marcas.length,
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
                        right:
                            new BorderSide(width: 1.0, color: Colors.white24))),
                child: GestureDetector(
                  child: Icon(Icons.delete, color: Colors.white),
                  onTap: () async {
                    bool success = (await MarcaRepository.remove(marcas[index].id));
                    if(success) {
                      Toast.show("Marca deletada com sucesso!", context, duration: Toast.LENGTH_LONG);
                      marcas.remove(marcas[index]);
                    } else {
                      Toast.show("Erro ao deletar. Tente novamente!", context, duration: Toast.LENGTH_LONG);
                    }
                  },
                )
              ),
              title: Text(
                marcas[index].nome,
                style:
                    TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
              ),
              subtitle: Row(
                children: <Widget>[
                  Icon(Icons.linear_scale, color: Colors.yellowAccent),
                  Text(" ID: " + marcas[index].id.toString(), style: TextStyle(color: Colors.white))
                ],
              ),
              trailing: GestureDetector(
                child: Icon(Icons.keyboard_arrow_right,
                    color: Colors.white, size: 30.0),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => MarcaForm(
                            marca: marcas[index],
                            title: "Editando Marca: " + marcas[index].nome)),
                  );
                },
              )
            ),
          ),
        );
      },
    );
  }
}

class MarcaForm extends StatefulWidget {
  final Marca marca;
  final String title;

  const MarcaForm({Key key, this.marca, this.title}) : super(key: key);

  @override
  MarcaFormState createState() => MarcaFormState(marca, title);
}

class MarcaFormState extends State<MarcaForm> {
  final _formKey = GlobalKey<FormState>();
  final Marca marca;
  final String title;

  final nomeController = TextEditingController();

  MarcaFormState(this.marca, this.title);

  @override
  Widget build(BuildContext context) {
    if(marca != null)
      nomeController.text = marca.nome;

    return new Scaffold(
      appBar: AppBar(title: Text(this.title)),
      body: new Container(
          padding: const EdgeInsets.all(30.0),
          color: Colors.white,
          child: new Container(
            child: new Center(
                child: new Column(children: [
              new Padding(padding: EdgeInsets.only(top: 40.0)),
              new Text(
                'Nome da Marca',
                style: new TextStyle(color: Colors.teal, fontSize: 25.0),
              ),
              new Padding(padding: EdgeInsets.only(top: 30.0)),
              new TextField(
                decoration: new InputDecoration(
                  labelText: "Informe o nome da marca",
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
          if (this.marca == null) {
            Marca marca = new Marca(id: 0, nome: nomeController.text);
            Future<Marca> novaMarca = (await MarcaRepository.create(marca)) as Future<Marca>;

            if(novaMarca != null) {
              Toast.show("Marca cadastrada com sucesso", context, duration: Toast.LENGTH_LONG);
              Navigator.pop(context);
            }else {
              Toast.show("Erro ao cadastrar nova marca. Tente novamente!", context, duration: Toast.LENGTH_LONG);
            }

          } else {}

          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => MarcaForm(marca: null)),
          );
        },
        label: Text("Salvar"),
        icon: Icon(Icons.save),
      ),
    );
  }
}
