package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInt {
    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;

    public MenuInt() {
        initialize();
    }

    private void initialize() {
        
        frame = new JFrame("Menú de Cafetería");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500); 
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); 

        
        JLabel titulo = new JLabel("POWER MENU ", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(139, 69, 19)); // Color café
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Espaciado
        frame.add(titulo, BorderLayout.NORTH);

        
        String[] columnNames = {"Producto", "Precio (MXN)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        
        cargarDatosMenu(model);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16)); 
        table.setRowHeight(30); // Filas más altas
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(210, 180, 140)); 
        table.setEnabled(false); 

        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        frame.add(scrollPane, BorderLayout.CENTER);

        
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 

        JButton btnCerrar = new JButton("Cerrar Menú");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(139, 69, 19));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panelBotones.add(btnCerrar);
        frame.add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatosMenu(DefaultTableModel model) {
        
        if (MenuAlimentos.obtenerCantidadProductos() == 0) {
            MenuAlimentos.cargarMenuDesdeCSV();
        }

        int cantidadProductos = MenuAlimentos.obtenerCantidadProductos();

        for (int i = 0; i < cantidadProductos; i++) {
            MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(i + 1);
            if (producto != null) {
                Object[] rowData = {
                        producto.nombre, // Solo el nombre
                        String.format("$%.2f", producto.precio) 
                };
                model.addRow(rowData);
            }
        }
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    
    public static void mostrarMenuGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new src.MenuInt().mostrar();
            }
        });
    }
}
