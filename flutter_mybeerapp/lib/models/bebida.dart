import 'package:flutter_mybeerapp/models/marca.dart';
import 'package:flutter_mybeerapp/models/modelo.dart';

class Bebida {
  final int id;
  final Marca marca;
  final Modelo modelo;

  Bebida({this.id, this.marca, this.modelo});

  factory Bebida.fromJson(Map<String, dynamic> json) {
    return Bebida(
        id: json['id'] as int,
        marca: Marca.fromJson(json['marca']),
        modelo: Modelo.fromJson(json['modelo'])
    );
  }

  Map<String, dynamic> toJSON() {
    return {
      "id": this.id,
      "marca": this.marca.id,
      "modelo": this.modelo.id
    };
  }

  @override
  String toString() {
    return this.marca.nome + " - " + this.modelo.toString();
  }

  bool operator == (o) => o is Bebida && o.id == id;

}