class Loja {
  final int id;
  final String nome;

  Loja({this.id, this.nome});

  factory Loja.fromJson(Map<String, dynamic> json) {
    return Loja(id: json['id'] as int, nome: json['nome']);
  }

  Map<String, dynamic> toJSON() {
    return {
      "id": this.id,
      "nome": this.nome
    };
  }

  @override
  String toString() {
    return this.nome;
  }

  bool operator == (o) => o is Loja && o.id == id;

}