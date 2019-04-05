import 'dart:convert';

import 'package:flutter_mybeerapp/models/marca.dart';
import 'package:http/http.dart' as http;

class MarcaRepository {
  static Future<List<Marca>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/marcas/');

    if (response.statusCode == 200) {
      List<Marca> marcas = new List();

      final marcasJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < marcasJson.length; x++)
        marcas.add(Marca.fromJson(marcasJson[x] as Map<String, dynamic>));

      return marcas;
    } else {
      throw Exception('Falha ao listar marcas');
    }
  }

  static Future<Marca> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/marcas/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Marca.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar marca');
    }
  }

  static Future<Marca> create(Marca marca) async {
    final response = await http.post(
      'https://sobral.pythonanywhere.com/marcas/',
      headers: {"Content-Type": "application/json"},
      body: json.encode(marca.toJSON())
    );

    if (response.statusCode == 201) {
      return Marca.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao salvar a nova marca');
    }
  }

  static Future<Marca> update(Marca marca) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/marcas/' + marca.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(marca.toJSON()));

    if (response.statusCode == 200) {
      return Marca.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/marcas/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}
