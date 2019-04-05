import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:flutter_mybeerapp/models/cesta.dart';
import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:intl/intl.dart';

class Produto {
  final int id;
  final int idProdutoCesta;
  final Bebida bebida;
  final Loja loja;
  final double precoUnidade;
  double precoLitro;
  final String ultimaAtualizacao;
  int quantidade;
  Cesta cesta;

  Produto(
      {this.precoUnidade,
      this.precoLitro,
      this.ultimaAtualizacao,
      this.id,
      this.bebida,
      this.loja,
      this.quantidade,
      this.cesta,
      this.idProdutoCesta});

  factory Produto.fromJson(Map<String, dynamic> json) {

    if(json == null) return null;

    return Produto(
        id: json['id'] as int,
        bebida: Bebida.fromJson(json['bebida']),
        loja: Loja.fromJson(json['loja']),
        precoLitro: json['preco_litro'] as double,
        precoUnidade: json['preco_unidade'] as double,
        ultimaAtualizacao: json['ultima_atualizacao'],
        quantidade: (json.containsKey('quantidade')) ? json['quantidade'] : 0,
        idProdutoCesta: (json.containsKey('id_relacionamento_produto_cesta')
            ? json['id_relacionamento_produto_cesta']
            : 0
        )
    );
  }

  Map<String, dynamic> toJSON() {
    var now = new DateTime.now();
    var formatter = new DateFormat('yyyy-MM-dd');

    calcularPrecoLitro();

    Map<String, dynamic> json = {
      "id": this.id,
      "bebida": this.bebida.id,
      "loja": this.loja.id,
      "preco_unidade": this.precoUnidade,
      "preco_litro": this.precoLitro,
      "ultima_atualizacao": formatter.format(now),
    };

    if (quantidade > 0) json.putIfAbsent("quantidade", quantidade as dynamic);

    return json;
  }

  void calcularPrecoLitro() {
    this.precoLitro = (precoUnidade / 1000) * bebida.modelo.volume;
  }

  @override
  String toString() {
    return this.bebida.toString();
  }

  bool operator ==(o) => o is Produto && o.id == id;
}
