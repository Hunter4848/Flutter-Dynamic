import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // Membuat channel untuk komunikasi dengan kode native
  static const platform = MethodChannel('everspin.id.flutterdynamic/channel');

  // Fungsi untuk memanggil method di kode native
  Future<void> _callNativeMethod() async {
    try {
      final String result = await platform.invokeMethod('nativeMethod');
      print('Result from native: $result');
    } on PlatformException catch (e) {
      print("Failed to call native method: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Flutter Native Code Example'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: _callNativeMethod,
            child: Text('Call Native Method'),
          ),
        ),
      ),
    );
  }
}
