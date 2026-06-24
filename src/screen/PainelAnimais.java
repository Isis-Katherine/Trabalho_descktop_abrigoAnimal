package screen;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import utils.BotaoDeletar;
import utils.BotaoEditar;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class PainelAnimais extends JPanel {

    private static final Color VERDE         = new Color(46, 125, 50);
    private static final Color VERDE_HOVER   = new Color(27, 94, 32);
    private static final Color VERDE_CLARO   = new Color(200, 230, 201);
    private static final Color VERDE_TEXTO   = new Color(27, 94, 32);
    private static final Color FUNDO         = new Color(248, 250, 248);
    private static final Color BRANCO        = Color.WHITE;
    private static final Color CINZA_BORDA   = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO   = new Color(100, 100, 100);
    private static final Color LARANJA       = new Color(230, 81, 0);
    private static final Color LARANJA_CLARO = new Color(255, 224, 178);

    private DefaultTableModel modelo;
    private JTable tabela;
    private JComboBox<String> comboEspecie;
    private JComboBox<String> comboPorte;
    private JComboBox<String> comboStatus;
    private JLabel lblInfoPaginacao;

    public PainelAnimais() {
        setLayout(new BorderLayout());
        setBackground(FUNDO);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(criarTopo(),  BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
    }

    // ── TOPO ─────────────────────────────────────────────────────────────────
    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout(0, 14));
        topo.setOpaque(false);

        JLabel titulo = new JLabel("Módulo: Animais - Listagem", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 40));

        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setOpaque(false);

        JTextField campo = new JTextField("Buscar animal...") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 24, 24));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(CINZA_TEXTO);
        campo.setBorder(new EmptyBorder(10, 16, 10, 16));
        campo.setOpaque(false);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {        

            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals("Buscar animal...")) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText("Buscar animal...");
                    campo.setForeground(CINZA_TEXTO);
                }
            }
        });

        JButton btnCadastrar = criarBotaoVerde("+ Cadastrar Animal");

        campo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            private void pesquisar() {

                if (tabela == null || modelo == null)
                    return;

                String texto = campo.getText();

                TableRowSorter<DefaultTableModel> sorter =
                        new TableRowSorter<>(modelo);

                tabela.setRowSorter(sorter);

                if (texto.trim().isEmpty() ||
                    texto.equals("Buscar animal...")) {

                    sorter.setRowFilter(null);

                } else {

                    sorter.setRowFilter(
                        RowFilter.regexFilter("(?i)" + texto)
                    );
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                pesquisar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                pesquisar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                pesquisar();
            }
        });

        btnCadastrar.addActionListener(e -> {
            Window janelaPai = SwingUtilities.getWindowAncestor(this);
            new DialogCadastroAnimal(janelaPai, this::atualizarTabela).setVisible(true);
        });

        barra.add(campo,        BorderLayout.CENTER);
        barra.add(btnCadastrar, BorderLayout.EAST);

        topo.add(titulo, BorderLayout.NORTH);
        topo.add(barra,  BorderLayout.CENTER);

        return topo;
    }

    // ── CORPO ─────────────────────────────────────────────────────────────────
    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(16, 0));
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(14, 0, 0, 0));
        corpo.add(criarFiltros(), BorderLayout.WEST);
        corpo.add(criarTabela(),  BorderLayout.CENTER);
        return corpo;
    }

    // ── FILTROS ──────────────────────────────────────────────────────────────
    private JPanel criarFiltros() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(BRANCO);
        painel.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(16, 14, 16, 14)
        ));
        painel.setPreferredSize(new Dimension(170, 0));

        JLabel lbl = new JLabel("Filtros");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(lbl);
        painel.add(Box.createVerticalStrut(14));

        comboEspecie = new JComboBox<>(new String[]{"Todas", "Cachorro", "Gato", "Coelho"});
        comboPorte   = new JComboBox<>(new String[]{"Todos", "Pequeno", "Médio", "Grande"});
        comboStatus  = new JComboBox<>(new String[]{"Todos", "Disponível", "Adotado"});

        comboEspecie.addActionListener(e -> aplicarFiltros());
        comboPorte.addActionListener(e -> aplicarFiltros());
        comboStatus.addActionListener(e -> aplicarFiltros());

        painel.add(grupoFiltro("Espécie", comboEspecie));
        painel.add(Box.createVerticalStrut(12));
        painel.add(grupoFiltro("Porte",   comboPorte));
        painel.add(Box.createVerticalStrut(12));
        painel.add(grupoFiltro("Status",  comboStatus));
        painel.add(Box.createVerticalStrut(12));

        JButton btnLimpar = new JButton("Limpar filtros");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpar.setBackground(new Color(238, 238, 238));
        btnLimpar.setForeground(new Color(60, 60, 60));
        btnLimpar.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(7, 12, 7, 12)
        ));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpar.setAlignmentX(LEFT_ALIGNMENT);
        btnLimpar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnLimpar.addActionListener(e -> limparFiltros());

        painel.add(btnLimpar);
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel grupoFiltro(String rotulo, JComboBox<String> combo) {
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        g.setOpaque(false);
        g.setAlignmentX(LEFT_ALIGNMENT);

        JLabel l = new JLabel(rotulo);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setAlignmentX(LEFT_ALIGNMENT);

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(BRANCO);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        combo.setAlignmentX(LEFT_ALIGNMENT);

        g.add(l);
        g.add(Box.createVerticalStrut(4));
        g.add(combo);

        return g;
    }

    // ── TABELA ───────────────────────────────────────────────────────────────
    private JPanel criarTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO);
        painel.setBorder(new LineBorder(CINZA_BORDA, 1, true));

        String[] colunas = {"Foto", "Nome", "Espécie", "Porte", "Status", "Ações"};
        
        List<data.Animal> lista = utils.SistemaDados.animais;
        Object[][] dados = new Object[lista.size()][6];
        for (int i = 0; i < lista.size(); i++) {
            data.Animal a = lista.get(i);
            dados[i][0] = "";
            dados[i][1] = a.getNome();
            dados[i][2] = a.geEspecie();
            dados[i][3] = a.getPorte();
            dados[i][4] = a.getAdotado() ? "Adotado" : "Disponível";
            dados[i][5] = "";
        }

        modelo = new DefaultTableModel(dados, colunas) {
            @Override public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(56);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setGridColor(new Color(240, 240, 240));
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(new Color(232, 245, 233));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFocusable(false);

        JTableHeader cab = tabela.getTableHeader();
        cab.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cab.setBackground(new Color(248, 248, 248));
        cab.setForeground(new Color(60, 60, 60));
        cab.setBorder(new MatteBorder(0, 0, 1, 0, CINZA_BORDA));
        cab.setPreferredSize(new Dimension(0, 42));

        int[] larguras = {66, 120, 110, 90, 120, 150};
        for (int i = 0; i < larguras.length; i++)
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);

        // Coluna foto →  ANIMAIS (AVATAR)

        tabela.getColumnModel().getColumn(0).setCellRenderer(
            (t, valor, selecionado, foco, row, col) -> {

                JLabel lbl = new JLabel();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                int modelRow =
                    tabela.convertRowIndexToModel(row);

                data.Animal animal =
                    utils.SistemaDados.animais.get(modelRow);

                String caminho =
                    animal.getCaminhoImagem();

                if (caminho != null && !caminho.isEmpty()) {

                    ImageIcon icon =
                        new ImageIcon(caminho);

                    Image img =
                        icon.getImage().getScaledInstance(
                            45,
                            45,
                            Image.SCALE_SMOOTH
                        );

                    lbl.setIcon(new ImageIcon(img));

                } else {

                    lbl.setText("🐾");
                }

                return lbl;
            }
        );

        // Coluna status → badge colorido
        tabela.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, row, col) -> {
            boolean adotado = "Adotado".equalsIgnoreCase(String.valueOf(v));
            JPanel badge = new JPanel(new GridBagLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(adotado ? LARANJA_CLARO : VERDE_CLARO);
                    g2.fillRoundRect(6, 10, getWidth() - 12, getHeight() - 20, 14, 14);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setBackground(sel ? new Color(232, 245, 233) : BRANCO);
            JLabel l = new JLabel(String.valueOf(v));
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setForeground(adotado ? LARANJA : VERDE_TEXTO);
            badge.add(l);
            return badge;
        });

        // Coluna ações → botões editar e deletar
        tabela.getColumnModel().getColumn(5).setCellRenderer(new AcoesCellRenderer());
        tabela.getColumnModel().getColumn(5).setCellEditor(new AcoesCellEditor(tabela, modelo));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BRANCO);

        painel.add(scroll,           BorderLayout.CENTER);
        painel.add(criarPaginacao(), BorderLayout.SOUTH);
        return painel;
    }

    // ── PAGINAÇÃO ─────────────────────────────────────────────────────────────
    private JPanel criarPaginacao() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(BRANCO);
        rodape.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, CINZA_BORDA),
            new EmptyBorder(8, 16, 8, 16)
        ));

        lblInfoPaginacao = new JLabel("Mostrando 1 a " + utils.SistemaDados.animais.size() + " de " + utils.SistemaDados.animais.size() + " animais");
        lblInfoPaginacao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoPaginacao.setForeground(CINZA_TEXTO);

        JPanel pags = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pags.setOpaque(false);
        for (int i = 0; i < 6; i++) {
            String t = i < 5 ? String.valueOf(i + 1) : "›";
            pags.add(criarBtnPagina(t, i == 0));
        }

        rodape.add(lblInfoPaginacao, BorderLayout.WEST);
        rodape.add(pags, BorderLayout.EAST);
        return rodape;
    }

    private JButton criarBtnPagina(String texto, boolean ativo) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ativo ? VERDE : new Color(240, 240, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(ativo ? BRANCO : CINZA_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(34, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── BOTÃO VERDE ───────────────────────────────────────────────────────────
    private JButton criarBotaoVerde(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(BRANCO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── RENDERER DAS AÇÕES ────────────────────────────────────────────────────
    private static class AcoesCellRenderer implements TableCellRenderer {
        private final JPanel painel      = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();

        public AcoesCellRenderer() {
            painel.setOpaque(true);
            estilizar(btnEditar,  AZUL);
            estilizar(btnDeletar, VERMELHO);
            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        private static final Color AZUL     = new Color(25, 118, 210);
        private static final Color VERMELHO = new Color(198, 40, 40);

        private void estilizar(JButton btn, Color cor) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setForeground(Color.WHITE);
            btn.setBackground(cor);
            btn.setBorder(new EmptyBorder(5, 10, 5, 10));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
        }

        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            painel.setBackground(sel ? new Color(232, 245, 233) : Color.WHITE);
            return painel;
        }
    }

    // ------------- APLICAR FILTROS ------------------
    private void aplicarFiltros() {
        if (tabela == null || modelo == null) return;
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        List<RowFilter<Object, Object>> filtros = new java.util.ArrayList<>();

        String especie = comboEspecie.getSelectedItem().toString();
        if (!especie.equals("Todas"))
            filtros.add(RowFilter.regexFilter("(?i)" + especie, 2)); // coluna Espécie

        String porte = comboPorte.getSelectedItem().toString();
        if (!porte.equals("Todos"))
            filtros.add(RowFilter.regexFilter("(?i)" + porte, 3)); // coluna Porte

        String status = comboStatus.getSelectedItem().toString();
        if (!status.equals("Todos"))
            filtros.add(RowFilter.regexFilter("(?i)" + status, 4)); // coluna Status

        if (filtros.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.andFilter(filtros));
    }

    // -------------- LIMPAR FILTROS --------------
    private void limparFiltros() {
        comboEspecie.setSelectedIndex(0);
        comboPorte.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        if (tabela != null) tabela.setRowSorter(null);
    }

    public void atualizarTabela() {
        if (modelo == null) return;
        modelo.setRowCount(0);
        List<data.Animal> lista = utils.SistemaDados.animais;
        for (data.Animal a : lista) {
            modelo.addRow(new Object[]{
                "",
                a.getNome(),
                a.geEspecie(),
                a.getPorte(),
                a.getAdotado() ? "Adotado" : "Disponível",
                ""
            });
        }
        if (lblInfoPaginacao != null) {
            int total = lista.size();
            lblInfoPaginacao.setText("Mostrando 1 a " + total + " de " + total + " animais");
        }
    }

    // ── EDITOR DAS AÇÕES ─────────────────────────────────────────────────────
    private class AcoesCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel painel      = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();
        private int linhaSelecionada;

        public AcoesCellEditor(JTable tabela, DefaultTableModel modelo) {
            painel.setOpaque(true);
            painel.setBackground(new Color(232, 245, 233));

            estilizar(btnEditar,  new Color(25, 118, 210));
            estilizar(btnDeletar, new Color(198, 40, 40));

            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                Window janelaPai = SwingUtilities.getWindowAncestor(tabela);
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Animal animal = utils.SistemaDados.animais.get(modelRow);
                new DialogEditarAnimal(janelaPai, animal, PainelAnimais.this::atualizarTabela).setVisible(true);
            });

            btnDeletar.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Animal animal = utils.SistemaDados.animais.get(modelRow);
                int confirmacao = JOptionPane.showConfirmDialog(tabela,
                    "Deseja deletar o animal \"" + animal.getNome() + "\"?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    utils.SistemaDados.animais.remove(animal);
                    utils.SistemaDados.save();
                    atualizarTabela();
                }
            });

            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        private void estilizar(JButton btn, Color cor) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setForeground(Color.WHITE); 
            btn.setBackground(cor);
            btn.setBorder(new EmptyBorder(5, 10, 5, 10));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
        }

        @Override public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            linhaSelecionada = row;
            return painel;
        }

        @Override public Object getCellEditorValue() { return ""; }
    }
}