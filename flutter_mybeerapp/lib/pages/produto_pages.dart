import 'package:flutter/material.dart';
import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:flutter_mybeerapp/models/produto.dart';
import 'package:flutter_mybeerapp/src/app.dart';
import 'package:flutter_mybeerapp/repositories/bebida_repository.dart';
import 'package:flutter_mybeerapp/repositories/produto_repository.dart';
import 'package:flutter_mybeerapp/repositories/loja_repository.dart';
import 'package:toast/toast.dart';

class ProdutoPage extends StatefulWidget {
  @override
  ProdutoPageState createState() => ProdutoPageState();
}

class ProdutoPageState extends State<ProdutoPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Produtos")),
      body: Center(
          child: FutureBuilder(
        future: ProdutoRepository.retrieveAll(),
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
                    ProdutoForm(produto: null, title: "Novo Produto")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Produto> produtos = snapshot.data;
    return new ListView.builder(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: produtos.length,
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
                        bool success = (await ProdutoRepository.remove(
                            produtos[index].id));
                        if (success) {
                          Toast.show("Produto deletado com sucesso!", context,
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
                  produtos[index].toString(),
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                subtitle: Row(
                  children: <Widget>[
                    Text(
                        produtos[index].loja.toString() +
                            " | R\$" +
                            produtos[index].precoUnidade.toStringAsFixed(2),
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
                          builder: (context) => ProdutoForm(
                              produto: produtos[index],
                              title: "Editando Produto: " +
                                  produtos[index].toString())),
                    );
                  },
                )),
          ),
        );
      },
    );
  }
}

class ProdutoForm extends StatefulWidget {
  final Produto produto;
  final String title;

  const ProdutoForm({Key key, this.produto, this.title}) : super(key: key);

  @override
  ProdutoFormState createState() => ProdutoFormState(produto, title);
}

class ProdutoFormState extends State<ProdutoForm> {
  final Produto produto;
  final String title;

  List<Bebida> _bebidas;
  List<Loja> _lojas;

  Bebida _selectedBebida;
  Loja _selectedLoja;

  List<DropdownMenuItem<Bebida>> _dropDownMenuBebidas;
  List<DropdownMenuItem<Loja>> _dropDownMenuLojas;

  final precoUnidadeController = TextEditingController();

  ProdutoFormState(this.produto, this.title);

  List<DropdownMenuItem<Bebida>> getDropDownMenuBebidas() {
    List<DropdownMenuItem<Bebida>> bebidas = new List();

    for (Bebida bebida in _bebidas) {
      bebidas.add(new DropdownMenuItem(
          value: bebida, child: new Text(bebida.toString())));
    }
    return bebidas;
  }

  List<DropdownMenuItem<Loja>> getDropDownMenuLojas() {
    List<DropdownMenuItem<Loja>> lojas = new List();

    for (Loja loja in _lojas) {
      lojas.add(
          new DropdownMenuItem(value: loja, child: new Text(loja.toString())));
    }
    return lojas;
  }

  void changedDropDownBebida(Bebida selectedBebida) {
    setState(() {
      _selectedBebida = selectedBebida;
    });
  }

  void changedDropDownLoja(Loja selectedLoja) {
    setState(() {
      _selectedLoja = selectedLoja;
    });
  }

  @override
  void initState() {
    precoUnidadeController.text = (this.produto != null)
        ? this.produto.precoUnidade.toStringAsFixed(2)
        : "0.00";
    // carregando comboboxes de bebidas
    BebidaRepository.retrieveAll().then(
        (bebidas) => {
              this._bebidas = bebidas,
              _dropDownMenuBebidas = getDropDownMenuBebidas(),
              (produto != null)
                  ? changedDropDownBebida(produto.bebida)
                  : changedDropDownBebida(bebidas[0])
            },
        onError: (e) => {});
    // carregando comboboxes de lojas
    LojaRepository.retrieveAll().then(
        (lojas) => {
              this._lojas = lojas,
              _dropDownMenuLojas = getDropDownMenuLojas(),
              (produto != null)
                  ? changedDropDownLoja(produto.loja)
                  : changedDropDownLoja(lojas[0])
            },
        onError: (e) => {});
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
                    new Container(
                      padding: new EdgeInsets.all(10.0),
                    ),
                    new Text(
                      'Selecione um loja',
                      style: new TextStyle(color: Colors.teal, fontSize: 20.0),
                    ),
                    new Container(
                      padding: new EdgeInsets.all(5.0),
                    ),
                    new DropdownButton(
                      value: _selectedLoja,
                      items: _dropDownMenuLojas,
                      onChanged: changedDropDownLoja,
                    ),
                    new Container(
                      padding: new EdgeInsets.all(20.0),
                    ),
                    new Text(
                      'Selecione uma bebida',
                      style: new TextStyle(color: Colors.teal, fontSize: 20.0),
                    ),
                    new Container(
                      padding: new EdgeInsets.all(5.0),
                    ),
                    new DropdownButton(
                      value: _selectedBebida,
                      items: _dropDownMenuBebidas,
                      onChanged: changedDropDownBebida,
                    ),
                    new Container(
                      padding: new EdgeInsets.all(15.0),
                    ),
                    new Padding(padding: EdgeInsets.only(top: 30.0)),
                    new TextField(
                      decoration: new InputDecoration(
                        labelText: "Informe o preço unitário do produto",
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
                      controller: precoUnidadeController,
                    ),
                  ],
                )),
              ))),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<Produto> futureProduto;
          if (this.produto == null) {
            Produto produto = new Produto(
                id: 0,
                bebida: _selectedBebida,
                loja: _selectedLoja,
                precoUnidade: double.parse(precoUnidadeController.text));
            futureProduto = ProdutoRepository.create(produto);
          } else {
            Produto bebida = new Produto(
                id: this.produto.id,
                bebida: _selectedBebida,
                loja: _selectedLoja,
                precoUnidade: double.parse(precoUnidadeController.text));
            futureProduto = ProdutoRepository.update(bebida);
          }

          futureProduto.then((Produto value) {
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
