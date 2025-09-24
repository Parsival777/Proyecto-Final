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

    public MenuInt() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame("Menú de Cafetería - POWER CAFE");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        frame.setContentPane(mainPanel);


        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(139, 69, 19));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titulo = new JLabel("POWER MENU", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Deliciosas opciones para disfrutar", JLabel.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitulo.setForeground(new Color(255, 248, 225));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        titlePanel.add(titulo, BorderLayout.CENTER);
        titlePanel.add(subtitulo, BorderLayout.SOUTH);
        mainPanel.add(titlePanel, BorderLayout.NORTH);


        String[] columnNames = {"Producto", "Precio (MXN)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cargarDatosMenu(model);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);


        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(139, 69, 19));
        table.getTableHeader().setForeground(Color.WHITE);


        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));


        JButton btnCerrar = new JButton("Cerrar Menú");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(139, 69, 19));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(120, 35));

        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        buttonPanel.add(btnCerrar);
        southPanel.add(buttonPanel, BorderLayout.CENTER);


        JLabel footer = new JLabel("© 2024 Power Café - Todos los derechos reservados", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 10));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        southPanel.add(footer, BorderLayout.SOUTH);

        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
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
                        producto.nombre,
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
                new MenuInt().mostrar();
            }
        });
    }
}
