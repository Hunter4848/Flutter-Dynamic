import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('everspin.id.flutterdynamic/channel');

  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  Future<void> _callNativeMethod(String username, String password) async {
    print('Calling native method with username: $username and password: $password');

    try {
      // Memanggil native method dengan username dan password
      final Map<dynamic, dynamic> result = await platform.invokeMethod(
        'nativeMethod',
        {
          'username': username,
          'password': password,
        },
      );

      // Konversi Map<dynamic, dynamic> ke Map<String, dynamic>
      final Map<String, dynamic> response = Map<String, dynamic>.from(result);

      print('Received response from native method: $response');

      // Ambil data dari hasil yang dikembalikan oleh native method
      final payload = response['payload'] as String?;
      final evToken = response['evToken'] as String?;
      final evEncDesc = response['evEncDesc'] as String?;

      print('Payload: $payload');
      print('EV Token: $evToken');
      print('EV Enc Desc: $evEncDesc');

      // Kirim data ke backend melalui POST request
      final backendResponse = await http.post(
        Uri.parse('http://10.8.12.37:9002/everspin/user_login_eversafe'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'payload': payload,
          'evToken': evToken,
          'evEncDesc': evEncDesc,
        }),
      );

      print('Response from backend: ${backendResponse.body}');

      // Periksa response dari server
      if (backendResponse.statusCode == 200) {
        print('Data successfully posted to backend.');
      } else {
        print('Failed to post data to backend: ${backendResponse.statusCode}');
      }
    } on PlatformException catch (e) {
      print("Failed to call native method: '${e.message}'.");
    } catch (e) {
      print('Unexpected error: $e');
    }
  }
@override
Widget build(BuildContext context) {
  return MaterialApp(
    home: Scaffold(
      appBar: AppBar(
        title: Text('Flutter Native Code Example'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _usernameController,
              decoration: InputDecoration(labelText: 'Username'),
            ),
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(labelText: 'Password'),
              obscureText: true,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                // Memanggil native method dengan input dari user
                final username = _usernameController.text;
                final password = _passwordController.text;
                print(
                    'Button pressed with username: $username and password: $password');
                _callNativeMethod(username, password);
              },
              child: Text('Submit'),
            ),
          ],
        ),
      ),
    ),
  );
}}
