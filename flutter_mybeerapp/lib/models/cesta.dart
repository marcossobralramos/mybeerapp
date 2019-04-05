import 'package:flutter_mybeerapp/models/produto.dart';

class Cesta {
  final int id;
  String descricao;
  double total;
  double litros;
  List<Produto> produtos;

  Cesta({
    this.total = 0,
    this.litros = 0,
    this.id,
    this.produtos,
    this.descricao
  });

  factory Cesta.fromJson(Map<String, dynamic> json) {
    List<dynamic> jsonProdutos = json['produtos'];

    Cesta cesta = new Cesta(
        id: json['id'] as int,
        descricao: json['descricao'],
    );

    cesta.produtos = new List<Produto>();

    for(int x = 0; x < jsonProdutos.length; x++)
      cesta.addProduto(Produto.fromJson(jsonProdutos[x]));

    return cesta;
  }

  Map<String, dynamic> toJSON() {
    // Passando lista vazia de produtos, pois os produtos são
    // adicionados separadamente na API
    List<dynamic> produtos;

    return {
      "id": this.id,
      "descricao": this.descricao,
      "total": this.total,
      "litros": this.litros,
      "produtos": produtos
    };
  }

  void addProduto(Produto produto) {
    if(produto == null)
      return;

    if(this.produtos.contains(produto))
      this.removeProduto(produto);

    produto.cesta = this;
    this.produtos.add(produto);
    this.total += produto.precoUnidade * produto.quantidade;
    this.litros += (produto.bebida.modelo.volume * produto.quantidade) / 1000;
  }

  bool removeProduto(Produto produto) {
    bool success = this.produtos.remove(produto);

    if(success) {
      this.total -= produto.precoUnidade;
      this.litros -= (produto.bebida.modelo.volume * produto.quantidade) / 1000;
    }

    return success;
  }

  @override
  String toString() {
    return this.descricao;
  }

  bool operator == (o) => o is Cesta && o.id == id;
}
