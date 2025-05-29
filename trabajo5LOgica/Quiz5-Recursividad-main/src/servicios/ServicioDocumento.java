package servicios;

import entidades.Documento;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ServicioDocumento {

    private static List<Documento> documentos = new ArrayList<>();

    public static List<Documento> getLista() {
        return documentos;
    }

    public static void cargar(String nombreArchivo) {
        var br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                var linea = br.readLine(); // lectura header
                linea = br.readLine();
                while (linea != null) {
                    var textos = linea.split(";");
                    // Suponiendo el orden: Apellido1;Apellido2;Nombre;Documento
                    Documento documento = new Documento(textos[2], textos[0], textos[1], textos[3]);
                    documentos.add(documento);
                    linea = br.readLine();
                }
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void mostrar(JTable tbl) {
        String[] encabezados = { "#", "Nombre", "Primer Apellido", "Segundo Apellido", "Documento" };
        String[][] datos = new String[documentos.size()][encabezados.length];

        for (int i = 0; i < documentos.size(); i++) {
            Documento d = documentos.get(i);
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = d.getApellido1();
            datos[i][2] = d.getApellido2();
            datos[i][3] = d.getNombre();
            datos[i][4] = d.getDocumento();
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, encabezados) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl.setModel(modelo);
    }

    private static boolean esMayor(Documento d1, Documento d2, int criterio) {
        String campo1 = obtenerCampo(d1, criterio);
        String campo2 = obtenerCampo(d2, criterio);
        return campo1.compareToIgnoreCase(campo2) > 0;
    }

    private static String obtenerCampo(Documento d, int criterio) {
        return switch (criterio) {
            case 0 -> d.getNombreCompleto();  // nombre completo
            case 1 -> d.getApellido1();       // primer apellido
            case 2 -> d.getApellido2();       // segundo apellido
            default -> "";
        };
    }

    private static void intercambiar(int i, int j) {
        Documento temp = documentos.get(i);
        documentos.set(i, documentos.get(j));
        documentos.set(j, temp);
    }

    // Ordenamiento Burbuja
    public static void ordenarBurbuja(int criterio) {
        int n = documentos.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (esMayor(documentos.get(j), documentos.get(j + 1), criterio)) {
                    intercambiar(j, j + 1);
                }
            }
        }
    }

    // Ordenamiento rápido (QuickSort)
    private static int particionar(int inicio, int fin, int criterio) {
        Documento pivote = documentos.get(fin);
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if (!esMayor(documentos.get(j), pivote, criterio)) {
                i++;
                intercambiar(i, j);
            }
        }
        intercambiar(i + 1, fin);
        return i + 1;
    }

    private static void quickSort(int inicio, int fin, int criterio) {
        if (inicio < fin) {
            int p = particionar(inicio, fin, criterio);
            quickSort(inicio, p - 1, criterio);
            quickSort(p + 1, fin, criterio);
        }
    }

    public static void ordenarRapido(int criterio) {
        quickSort(0, documentos.size() - 1, criterio);
    }

    // Ordenamiento Inserción iterativo
    public static void ordenarInsercion(int criterio) {
        for (int i = 1; i < documentos.size(); i++) {
            Documento actual = documentos.get(i);
            int j = i - 1;
            while (j >= 0 && esMayor(documentos.get(j), actual, criterio)) {
                documentos.set(j + 1, documentos.get(j));
                j--;
            }
            documentos.set(j + 1, actual);
        }
    }

    // Ordenamiento Inserción recursivo
    private static void ordenarInsercionRecursivo(int n, int criterio) {
        if (n <= 0)
            return;
        ordenarInsercionRecursivo(n - 1, criterio);

        Documento actual = documentos.get(n);
        int j = n - 1;
        while (j >= 0 && esMayor(documentos.get(j), actual, criterio)) {
            documentos.set(j + 1, documentos.get(j));
            j--;
        }
        documentos.set(j + 1, actual);
    }

    public static void ordenarInsercionRecursivo(int criterio) {
        ordenarInsercionRecursivo(documentos.size() - 1, criterio);
    }

    public static int getTamaño() {
        return documentos.size();
    }

    public static int getIndice(Documento d) {
        return documentos.indexOf(d);
    }

   
    public static Documento buscarDocumentoBinario(String textoBusqueda, int inicio, int fin, int criterio) {
        if (inicio > fin) {
            return null;
        }

        int medio = (inicio + fin) / 2;
        Documento doc = documentos.get(medio);

        String valorCampo = obtenerCampo(doc, criterio).toLowerCase();

        int comparacion = valorCampo.compareTo(textoBusqueda);

        if (comparacion == 0) {
            return doc;
        } else if (comparacion > 0) {
            return buscarDocumentoBinario(textoBusqueda, inicio, medio - 1, criterio);
        } else {
            return buscarDocumentoBinario(textoBusqueda, medio + 1, fin, criterio);
        }
    }
} 

