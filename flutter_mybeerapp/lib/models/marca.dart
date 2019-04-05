class Marca {
  final int id;
  final String nome;

  Marca({this.id, this.nome});

  factory Marca.fromJson(Map<String, dynamic> json) {
    return Marca(id: json['id'] as int, nome: json['nome']);
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

  bool operator == (o) => o is Marca && o.id == id;

}
