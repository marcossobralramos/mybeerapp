import 'dart:convert';

import 'package:flutter_mybeerapp/models/loja.dart';
import 'package:flutter_mybeerapp/models/produto.dart';
import 'package:http/http.dart' as http;

class ProdutoRepository {
  static Future<List<Produto>> retrieveAll() async {
    final response = await http.get('https://sobral.pythonanywhere.com/produtos/');

    if (response.statusCode == 200) {
      List<Produto> produtos = new List();

      final produtosJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < produtosJson.length; x++)
        produtos.add(Produto.fromJson(produtosJson[x] as Map<String, dynamic>));

      return produtos;
    } else {
      throw Exception('Falha ao listar produtos');
    }
  }

  static Future<List<Produto>> retrieveByLoja(Loja loja) async {
    final response = await http.get('https://sobral.pythonanywhere.com/produtos/');

    if (response.statusCode == 200) {
      List<Produto> produtos = new List();

      final produtosJson = json.decode(response.body) as List<dynamic>;
      for (int x = 0; x < produtosJson.length; x++) {
        Produto produto = Produto.fromJson(produtosJson[x] as Map<String, dynamic>);
        if(produto.loja == loja)
          produtos.add(produto);
      }

      return produtos;
    } else {
      throw Exception('Falha ao listar produtos');
    }
  }

  static Future<Produto> retrieveById(int id) async {
    final response = await http
        .get('https://sobral.pythonanywhere.com/produtos/' + id.toString() + "/");

    if (response.statusCode == 200) {
      return Produto.fromJson(json.decode(response.body));
    } else {
      throw Exception('Falha ao carregar produto');
    }
  }

  static Future<Produto> create(Produto produto) async {
    final response = await http.post(
        'https://sobral.pythonanywhere.com/produtos/',
        headers: {"Content-Type": "application/json"},
        body: json.encode(produto.toJSON())
    );

    if (response.statusCode == 201) {
      return Produto.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<Produto> update(Produto produto) async {
    final response = await http
        .patch('https://sobral.pythonanywhere.com/produtos/' + produto.id.toString()
        + "/",
        headers: {"Content-Type": "application/json"},
        body: json.encode(produto.toJSON()));

    if (response.statusCode == 200) {
      return Produto.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body);
    }
  }

  static Future<bool> remove(int id) async {
    final response = await http
        .delete('https://sobral.pythonanywhere.com/produtos/' + id.toString() + "/");

    return (response.statusCode == 204) ? true : false;
  }

}