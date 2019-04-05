import 'dart:convert';

import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:http/http.dart' as http;

class LojaRepository {
  static Future<List<Loja>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/lojas/');

    if (response.statusCode == 200) {
      List<Loja> lojas = new List();

      final lojasJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < lojasJson.length; x++)
        lojas.add(Loja.fromJson(lojasJson[x] as Map<String, dynamic>));

      return lojas;
    } else {
      throw Exception('Falha ao listar lojas');
    }
  }

  static Future<Loja> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/lojas/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Loja.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar loja');
    }
  }

  static Future<Loja> create(Loja loja) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/lojas/',
        headers: {"Content-Type": "application/json"},
        body: json.encode(loja.toJSON())
    );

    if (response.statusCode == 201) {
      return Loja.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao salvar a nova loja');
    }
  }

  static Future<Loja> update(Loja loja) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/lojas/' + loja.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(loja.toJSON()));

    if (response.statusCode == 200) {
      return Loja.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/lojas/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}