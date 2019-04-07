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

class ProdutosCestaPage extends StatefulWidget {
  final Cesta _cesta;

  ProdutosCestaPage(this._cesta);

  @override
  ProdutosCestaPageState createState() => ProdutosCestaPageState(this._cesta);
}

class ProdutosCestaPageState extends State<ProdutosCestaPage> {
  Cesta _cesta;

  ProdutosCestaPageState(this._cesta);

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
        title: Text('Cesta: ' + _cesta.descricao,
            style: TextStyle(color: Colors.teal, fontWeight: FontWeight.w700)),
      ),
      body: Center(
          child: FutureBuilder(
        future: CestaRepository.retrieveProdutos(_cesta.id),
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
                    ProdutoForm(cesta: _cesta, produto: null, title: "Novo Produto")),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }

  _createListView(BuildContext context, AsyncSnapshot snapshot) {
    List<Produto> produtos = snapshot.data;

    if(produtos == null || produtos.length == 0)
      return new Text("Não há produtos nesta cesta");

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
                        bool success = (await CestaRepository.removeProduto(
                            produtos[index]));
                        if (success) {
                          Toast.show("Produto removido da cesta com sucesso!", context,
                              duration: Toast.LENGTH_LONG);
                          setState(() {});
                        } else {
                          Toast.show(
                              "Erro ao remover produto. Tente novamente!", context,
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
                        "Loja: " + produtos[index].loja.toString() +
                            "\nPreço unitário: R\$" +
                            produtos[index].precoUnidade.toStringAsFixed(2) +
                            "\nPreço por litro: R\$" +
                            produtos[index].precoLitro.toStringAsFixed(2) +
                            "\nQuantidade: " + produtos[index].quantidade.toString()
                            + " itens" + "\nTotal em litros: " +
                            produtos[index].totalLitros.toStringAsFixed(3) + "L" +
                            "\nTotal do pack: R\$" + produtos[index].totalPack.toStringAsFixed(2),
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
                              cesta: _cesta,
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
  final Cesta cesta;

  const ProdutoForm({Key key, this.produto, this.title, this.cesta}) : super(key: key);

  @override
  ProdutoFormState createState() => ProdutoFormState(produto, title, cesta);
}

class ProdutoFormState extends State<ProdutoForm> {
  Produto _produto;
  final Cesta _cesta;
  final String title;

  List<Produto> _produtos;
  List<Loja> _lojas;

  Produto _selectedProduto;
  Loja _selectedLoja;

  List<DropdownMenuItem<Produto>> _dropDownMenuProdutos;
  List<DropdownMenuItem<Loja>> _dropDownMenuLojas;

  final precoUnidadeController = TextEditingController();
  final quantidadeController = TextEditingController();

  // construtor
  ProdutoFormState(this._produto, this.title, this._cesta);

  List<DropdownMenuItem<Produto>> getDropDownMenuProdutos() {
    List<DropdownMenuItem<Produto>> produtos = new List();

    for (Produto produto in _produtos) {
      produtos.add(new DropdownMenuItem(
          value: produto, child: new Text(produto.toString())));
    }
    return produtos;
  }

  List<DropdownMenuItem<Loja>> getDropDownMenuLojas() {
    List<DropdownMenuItem<Loja>> lojas = new List();

    for (Loja loja in _lojas) {
      lojas.add(
          new DropdownMenuItem(value: loja, child: new Text(loja.toString())));
    }
    return lojas;
  }

  void changedDropDownProduto(Produto selectedProduto) {
    setState(() {
      _selectedProduto = selectedProduto;
    });
  }

  void changedDropDownLoja(Loja selectedLoja) {
    setState(() {
      _selectedLoja = selectedLoja;
      // carregando comboboxes de produtos
      ProdutoRepository.retrieveByLoja(_selectedLoja).then(
              (produtos) => {
          this._produtos = produtos,
          _dropDownMenuProdutos = getDropDownMenuProdutos(),
          (this._produto != null)
              ? changedDropDownProduto(this._produto)
              : changedDropDownProduto(this._produtos[0]),
          (this._produto != null)
            ? precoUnidadeController.text = this._produto.precoUnidade.toStringAsFixed(2)
            : precoUnidadeController.text = this._produtos[0].precoUnidade.toStringAsFixed(2)
          },
          onError: (e) => {});
    });
  }

  @override
  void initState() {
    quantidadeController.text = (this._produto != null)
        ? this._produto.quantidade.toString()
        : "0";
    // carregando comboboxes de lojas
    LojaRepository.retrieveAll().then(
        (lojas) => {
              this._lojas = lojas,
              _dropDownMenuLojas = getDropDownMenuLojas(),
              (_produto != null)
                  ? changedDropDownLoja(_produto.loja)
                  : changedDropDownLoja(lojas[0])
            },
        onError: (e) => {});
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        backgroundColor: Colors.transparent,
        leading: IconButton(
          color: Colors.teal,
          onPressed: () => Navigator.of(context).pop(),
          icon: Icon(Icons.arrow_back, color: Colors.teal),
        ),
        title: Text('Adicionando produto à cesta ',
            style: TextStyle(color: Colors.teal, fontWeight: FontWeight.w700)),
      ),
      body: new Container(
          padding: const EdgeInsets.all(10.0),
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
                      padding: new EdgeInsets.all(10.0),
                    ),
                    new Text(
                      'Selecione um produto',
                      style: new TextStyle(color: Colors.teal, fontSize: 20.0),
                    ),
                    new Container(
                      padding: new EdgeInsets.all(5.0),
                    ),
                    new DropdownButton(
                      value: _selectedProduto,
                      items: _dropDownMenuProdutos,
                      onChanged: changedDropDownProduto,
                    ),
                    new Container(
                      padding: new EdgeInsets.all(10.0),
                    ),
                    new Padding(padding: EdgeInsets.only(top: 10.0)),
                    new TextField(
                      decoration: new InputDecoration(
                        labelText: "Informe o preço unitário do produto (R\$)",
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
                    new Padding(padding: EdgeInsets.only(top: 10.0)),
                    new TextField(
                      decoration: new InputDecoration(
                        labelText: "Informe a quantidade do produto",
                        fillColor: Colors.white,
                        border: new OutlineInputBorder(
                          borderRadius: new BorderRadius.circular(25.0),
                          borderSide: new BorderSide(),
                        ),
                        //fillColor: Colors.green
                      ),
                      keyboardType: TextInputType.numberWithOptions(decimal: false),
                      style: new TextStyle(
                        fontFamily: "Poppins",
                      ),
                      controller: quantidadeController,
                    ),
                  ],
                )),
              ))),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          Future<bool> success;
          if (this._produto == null) {
            _selectedProduto.quantidade = int.parse(quantidadeController.text);
            _selectedProduto.precoUnidade = double.parse(precoUnidadeController.text);
            _selectedProduto.cesta = this._cesta;
            success = CestaRepository.addProduto(_selectedProduto);
          } else {
            _produto.quantidade = quantidadeController.text as int;
            _produto.precoUnidade = precoUnidadeController.text as double;
            success = CestaRepository.updateProduto(_produto);
          }

          success.then((bool value) {
            if (value == true) {
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
        label: Text("Adicionar"),
        icon: Icon(Icons.add),
      ),
    );
  }
}
