import 'dart:convert';

import 'package:flutter_mybeerapp/models/cesta.dart';
import 'package:flutter_mybeerapp/models/produto.dart';
import 'package:http/http.dart' as http;

class CestaRepository {
  static Future<List<Cesta>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/cestas/');

    if (response.statusCode == 200) {
      List<Cesta> cestas = new List();

      final cestasJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < cestasJson.length; x++)
        cestas.add(Cesta.fromJson(cestasJson[x] as Map<String, dynamic>));

      return cestas;
    } else {
      throw Exception('Falha ao listar cestas');
    }
  }

  static Future<bool> addProduto(Produto produto) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/produtos_cesta/',
        headers: {"Content-Type": "application/json"},
        body: json.encode({
          "cesta": produto.cesta.id,
          "produto": produto.id,
          "preco": produto.precoUnidade,
          "quantidade": produto.quantidade
        })
    );

    if (response.statusCode == 200) {
      return true;
    } else {
      throw Exception('Falha ao listar produtos');
    }
  }

  static Future<Cesta> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/cestas/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Cesta.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar cesta');
    }
  }

  static Future<Cesta> create(Cesta cesta) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/cestas/',
        headers: {"Content-Type": "application/json"},
        body: json.encode(cesta.toJSON())
    );

    if (response.statusCode == 201) {
      return Cesta.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<Cesta> update(Cesta cesta) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/cestas/' + cesta.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(cesta.toJSON()));

    if (response.statusCode == 200) {
      return Cesta.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/cestas/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

  static Future<bool> removeProduto(Produto produto) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/produtos_cesta/' +
          produto.idProdutoCesta.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}