import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:flutter_mybeerapp/models/cesta.dart';
import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:intl/intl.dart';

class Produto {
  final int id;
  final int idProdutoCesta;
  Bebida bebida;
  Loja loja;
  double precoUnidade;
  String ultimaAtualizacao;
  int quantidade;
  Cesta cesta;

  Produto(
      {this.precoUnidade,
      this.ultimaAtualizacao,
      this.id,
      this.bebida,
      this.loja,
      this.quantidade,
      this.cesta,
      this.idProdutoCesta});

  double get totalLitros => quantidade * this.bebida.modelo.volume / 1000;

  double get precoLitro => (precoUnidade / bebida.modelo.volume) * 1000;

  double get totalPack => quantidade * precoUnidade;

  factory Produto.fromJson(Map<String, dynamic> json) {

    if(json == null) return null;

    return Produto(
        id: json['id'] as int,
        bebida: Bebida.fromJson(json['bebida']),
        loja: Loja.fromJson(json['loja']),
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
    this.ultimaAtualizacao = formatter.format(now);

    Map<String, dynamic> json = {
      "bebida": this.bebida.id,
      "loja": this.loja.id,
      "preco_unidade": this.precoUnidade,
      "preco_litro": this.precoLitro,
      "ultima_atualizacao": this.ultimaAtualizacao,
    };

    if (this.quantidade != null && this.quantidade != 0)
      json.putIfAbsent("quantidade", quantidade as dynamic);

    return json;
  }

  @override
  String toString() {
    return this.bebida.toString();
  }

  bool operator == (o) => o is Produto && o.id == id;

  compareTo(Produto b) {
    return (this.precoLitro > b.precoLitro) ? 1
        : (this.precoLitro < b.precoLitro) ? -1 : 0;
  }
}
