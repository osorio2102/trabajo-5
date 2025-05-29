import entidades.Documento;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import servicios.ServicioDocumento;
import servicios.Util;

public class FrmOrdenamiento extends JFrame {

    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;

    private JButton btnBuscar;
    private JButton btnAnterior;
    private JButton btnSiguiente;

    private JToolBar tbOrdenamiento;
    private JComboBox<String> cmbCriterio;
    private JTextField txtTiempo;
    private JTextField txtBuscar;
    private JTable tblDocumentos;

   
    private int indiceActualBusqueda = -1;

    public FrmOrdenamiento() {

        tbOrdenamiento = new JToolBar();

        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();

        cmbCriterio = new JComboBox<>();
        txtTiempo = new JTextField();
        txtTiempo.setColumns(10);
        txtTiempo.setEditable(false);

        btnBuscar = new JButton();
        txtBuscar = new JTextField();
        txtBuscar.setColumns(15);

        btnAnterior = new JButton();
        btnSiguiente = new JButton();

        tblDocumentos = new JTable();

        setSize(700, 450);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

       
        btnOrdenarBurbuja.setIcon(crearIconoEscalado("/iconos/Ordenar.png", 24, 24));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(evt -> btnOrdenarBurbujaClick(evt));
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(crearIconoEscalado("/iconos/OrdenarRapido.png", 24, 24));
        btnOrdenarRapido.setToolTipText("Ordenar Rápido");
        btnOrdenarRapido.addActionListener(evt -> btnOrdenarRapidoClick(evt));
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(crearIconoEscalado("/iconos/OrdenarInsercion.png", 24, 24));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(evt -> btnOrdenarInsercionClick(evt));
        tbOrdenamiento.add(btnOrdenarInsercion);

    
        cmbCriterio.setModel(new DefaultComboBoxModel<>(
                new String[]{"Nombre Completo", "Nombre"}));
        tbOrdenamiento.add(cmbCriterio);

        tbOrdenamiento.add(txtTiempo);

        btnBuscar.setIcon(crearIconoEscalado("/iconos/Buscar.png", 24, 24));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(evt -> btnBuscarClick(evt));
        tbOrdenamiento.add(btnBuscar);

        tbOrdenamiento.add(txtBuscar);

       
        btnAnterior.setIcon(crearIconoEscalado("/iconos/Anterior.png", 24, 24));
        btnAnterior.setToolTipText("Buscar Anterior");
        btnAnterior.addActionListener(evt -> btnAnteriorClick(evt));
        tbOrdenamiento.add(btnAnterior);

       
        btnSiguiente.setIcon(crearIconoEscalado("/iconos/Siguiente.png", 24, 24));
        btnSiguiente.setToolTipText("Buscar Siguiente");
        btnSiguiente.addActionListener(evt -> btnSiguienteClick(evt));
        tbOrdenamiento.add(btnSiguiente);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

     
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Datos.csv";
        ServicioDocumento.cargar(nombreArchivo);
        ServicioDocumento.mostrar(tblDocumentos);
    }

    
    private ImageIcon crearIconoEscalado(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
        var imagen = icono.getImage();
        var imagenEscalada = imagen.getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        int criterio = cmbCriterio.getSelectedIndex();
        if (criterio >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarBurbuja(criterio);
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        int criterio = cmbCriterio.getSelectedIndex();
        if (criterio >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarRapido(criterio);
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {
        int criterio = cmbCriterio.getSelectedIndex();
        if (criterio >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarInsercionRecursivo(criterio);
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        }
    }

    private void btnBuscarClick(ActionEvent evt) {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        int criterio = cmbCriterio.getSelectedIndex();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un texto de búsqueda.");
            return;
        }
        if (criterio < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un criterio de búsqueda.");
            return;
        }

        
        ServicioDocumento.ordenarInsercion(criterio);
        ServicioDocumento.mostrar(tblDocumentos);

        Documento encontrado = ServicioDocumento.buscarDocumentoBinario(textoBusqueda, 0, ServicioDocumento.getTamaño() - 1, criterio);

        if (encontrado != null) {
            indiceActualBusqueda = ServicioDocumento.getIndice(encontrado);
            seleccionarFila(indiceActualBusqueda);
        } else {
            JOptionPane.showMessageDialog(this, "Documento no encontrado.");
            indiceActualBusqueda = -1;
        }
    }

    private void btnAnteriorClick(ActionEvent evt) {
        if (indiceActualBusqueda == -1) {
            JOptionPane.showMessageDialog(this, "Primero realiza una búsqueda válida.");
            return;
        }
        if (indiceActualBusqueda <= 0) {
            JOptionPane.showMessageDialog(this, "No hay documentos anteriores.");
            return;
        }
        indiceActualBusqueda--;
        seleccionarFila(indiceActualBusqueda);
    }

    private void btnSiguienteClick(ActionEvent evt) {
        if (indiceActualBusqueda == -1) {
            JOptionPane.showMessageDialog(this, "Primero realiza una búsqueda válida.");
            return;
        }
        if (indiceActualBusqueda >= ServicioDocumento.getTamaño() - 1) {
            JOptionPane.showMessageDialog(this, "No hay documentos siguientes.");
            return;
        }
        indiceActualBusqueda++;
        seleccionarFila(indiceActualBusqueda);
    }

    private void seleccionarFila(int fila) {
        tblDocumentos.setRowSelectionInterval(fila, fila);
        tblDocumentos.scrollRectToVisible(tblDocumentos.getCellRect(fila, 0, true));
    }
}





