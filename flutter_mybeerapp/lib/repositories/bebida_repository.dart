import 'dart:convert';

import 'package:flutter_mybeerapp/models/bebida.dart';
import 'package:http/http.dart' as http;

class BebidaRepository {
  static Future<List<Bebida>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/bebidas/');

    if (response.statusCode == 200) {
      List<Bebida> bebidas = new List();

      final bebidasJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < bebidasJson.length; x++)
        bebidas.add(Bebida.fromJson(bebidasJson[x] as Map<String, dynamic>));

      return bebidas;
    } else {
      throw Exception('Falha ao listar bebidas');
    }
  }

  static Future<Bebida> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/bebidas/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Bebida.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar bebida');
    }
  }

  static Future<Bebida> create(Bebida bebida) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/bebidas/',
        headers: {"Content-Type": "application/json"},
        body: json.encode(bebida.toJSON())
    );

    if (response.statusCode == 201) {
      return Bebida.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao salvar a nova bebida');
    }
  }

  static Future<Bebida> update(Bebida bebida) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/bebidas/' + bebida.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(bebida.toJSON()));

    if (response.statusCode == 200) {
      return Bebida.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/bebidas/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}