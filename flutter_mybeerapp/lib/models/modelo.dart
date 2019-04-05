class Modelo {
  final int id;
  final String nome;
  final int volume;

  Modelo({this.id, this.nome, this.volume});

  factory Modelo.fromJson(Map<String, dynamic> json) {
    return Modelo(
        id: json['id'] as int,
        nome: json['nome'],
        volume: json['volume'] as int
    );
  }

  Map<String, dynamic> toJSON() {
    return {
      "id": this.id,
      "nome": this.nome,
      "volume": this.volume
    };
  }

  @override
  String toString() {
    return this.nome + " - " + this.volume.toString() + "ml";
  }

  bool operator == (o) => o is Modelo && o.id == id;

}