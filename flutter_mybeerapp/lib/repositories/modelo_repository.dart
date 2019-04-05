import 'dart:convert';

import 'package:flutter_mybeerapp/models/modelo.dart';
import 'package:http/http.dart' as http;

class ModeloRepository {
  static Future<List<Modelo>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/modelos/');

    if (response.statusCode == 200) {
      List<Modelo> modelos = new List();

      final modelosJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < modelosJson.length; x++)
        modelos.add(Modelo.fromJson(modelosJson[x] as Map<String, dynamic>));

      return modelos;
    } else {
      throw Exception('Falha ao listar modelos');
    }
  }

  static Future<Modelo> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/modelos/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Modelo.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar modelo');
    }
  }

  static Future<Modelo> create(Modelo modelo) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/modelos/',
        headers: {"Content-Type": "application/json"},
        body: json.encode(modelo.toJSON())
    );

    if (response.statusCode == 201) {
      return Modelo.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao salvar a nova modelo');
    }
  }

  static Future<Modelo> update(Modelo modelo) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/modelos/' + modelo.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(modelo.toJSON()));

    if (response.statusCode == 200) {
      return Modelo.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/modelos/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}