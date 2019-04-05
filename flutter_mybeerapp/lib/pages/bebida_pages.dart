import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:flutter_mybeerapp/models/marca.dart';
import 'package:flutter_mybeerapp/models/modelo.dart';
import 'package:flutter_mybeerapp/src/app.dart';
import 'package:flutter_mybeerapp/repositories/bebida_repository.dart';
import 'package:flutter_mybeerapp/repositories/marca_repository.dart';
import 'package:flutter_mybeerapp/repositories/modelo_repository.dart';
import 'package:toast/toast.dart';

class BebidaPage extends StatefulWidget {
  @override
  BebidaPageState createState() => BebidaPageState();
}

class BebidaPageState extends State<BebidaPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Bebidas")),
      body: Center(
          child: FutureBuilder(
        future: BebidaRepository.retrieveAll(),
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
                    BebidaForm(bebida: null, title: "Nova Bebida")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Bebida> bebidas = snapshot.data;
    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: bebidas.length,
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
                            (await BebidaRepository.remove(bebidas[index].id));
                        if (success) {
                          Toast.show("Bebida deletada com sucesso!", context,
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
                  bebidas[index].toString(),
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                subtitle: Row(
                  children: <Widget>[
                    Icon(Icons.linear_scale, color: Colors.yellowAccent),
                    Text(" ID: " + bebidas[index].id.toString(),
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
                          builder: (context) => BebidaForm(
                              bebida: bebidas[index],
                              title: "Editando Bebida: " +
                                  bebidas[index].toString())),
                    );
                  },
                )),
          ),
        );
      },
    );
  }
}

class BebidaForm extends StatefulWidget {
  final Bebida bebida;
  final String title;

  const BebidaForm({Key key, this.bebida, this.title}) : super(key: key);

  @override
  BebidaFormState createState() => BebidaFormState(bebida, title);
}

class BebidaFormState extends State<BebidaForm> {
  final Bebida bebida;
  final String title;

  List<Marca> _marcas;
  List<Modelo> _modelos;

  Marca _selectedMarca;
  Modelo _selectedModelo;

  List<DropdownMenuItem<Marca>> _dropDownMenuMarcas;
  List<DropdownMenuItem<Modelo>> _dropDownMenuModelos;

  BebidaFormState(this.bebida, this.title);

  List<DropdownMenuItem<Marca>> getDropDownMenuMarcas() {
    List<DropdownMenuItem<Marca>> marcas = new List();

    for (Marca marca in _marcas) {
      marcas.add(new DropdownMenuItem(
          value: marca, child: new Text(marca.toString())));
    }
    return marcas;
  }

  List<DropdownMenuItem<Modelo>> getDropDownMenuModelos() {
    List<DropdownMenuItem<Modelo>> modelos = new List();

    for (Modelo modelo in _modelos) {
      modelos.add(new DropdownMenuItem(
          value: modelo, child: new Text(modelo.toString())));
    }
    return modelos;
  }

  void changedDropDownMarca(Marca selectedMarca) {
    setState(() {
      _selectedMarca = selectedMarca;
    });
  }

  void changedDropDownModelo(Modelo selectedModelo) {
    setState(() {
      _selectedModelo = selectedModelo;
    });
  }

  @override
  void initState() {
    // carregando comboboxes de marcas
    MarcaRepository.retrieveAll().then(
        (marcas) => {
              this._marcas = marcas,
              _dropDownMenuMarcas = getDropDownMenuMarcas(),
              (bebida != null)
                  ? changedDropDownMarca(bebida.marca)
                  : changedDropDownMarca(marcas[0])
            },
        onError: (e) => {});
    // carregando comboboxes de modelos
    ModeloRepository.retrieveAll().then(
        (modelos) => {
              this._modelos = modelos,
              _dropDownMenuModelos = getDropDownMenuModelos(),
              (bebida != null)
                  ? changedDropDownModelo(bebida.modelo)
                  : changedDropDownModelo(modelos[0])
            },
        onError: (e) => {});
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(title: Text(this.title)),
      body: new Container(
          padding: const EdgeInsets.all(30.0),
          color: Colors.white,
          child: new Container(
              padding: const EdgeInsets.all(30.0),
              color: Colors.white,
              child: new Container(
                child: new Center(
                    child: new Column(
                  children: <Widget>[
                    new Padding(padding: EdgeInsets.only(top: 20.0)),
                    new Text(
                      'Selecione uma marca',
                      style: new TextStyle(color: Colors.teal, fontSize: 25.0),
                    ),
                    new Container(
                      padding: new EdgeInsets.all(5.0),
                    ),
                    new DropdownButton(
                      value: _selectedMarca,
                      items: _dropDownMenuMarcas,
                      onChanged: changedDropDownMarca,
                    ),
                    new Container(
                      padding: new EdgeInsets.all(15.0),
                    ),
                    new Text(
                      'Selecione um modelo',
                      style: new TextStyle(color: Colors.teal, fontSize: 25.0),
                    ),
                    new Container(
                      padding: new EdgeInsets.all(5.0),
                    ),
                    new DropdownButton(
                      value: _selectedModelo,
                      items: _dropDownMenuModelos,
                      onChanged: changedDropDownModelo,
                    )
                  ],
                )),
              ))),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<Bebida> futureBebida;
          if (this.bebida == null) {
            Bebida bebida = new Bebida(
                id: 0, marca: _selectedMarca, modelo: _selectedModelo);
            futureBebida = BebidaRepository.create(bebida);
          } else {
            Bebida marca = new Bebida(
                id: this.bebida.id,
                marca: _selectedMarca,
                modelo: _selectedModelo);
            futureBebida = BebidaRepository.update(marca);
          }

          futureBebida.then((Bebida value) {
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
