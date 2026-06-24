package screen;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import utils.BotaoEditar;
import utils.BotaoDeletar;

public class PainelAdotante extends JPanel {

    private static final Color VERDE       = new Color(46, 125, 50);
    private static final Color VERDE_HOVER = new Color(27, 94, 32);
    private static final Color FUNDO       = new Color(248, 250, 248);
    private static final Color BRANCO      = Color.WHITE;
    private static final Color CINZA_BORDA = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO = new Color(100, 100, 100);

    // Atributos da classe para o filtro ter acesso
    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel lblInfoPaginacao;

    public PainelAdotante() {
        setLayout(new BorderLayout());
        setBackground(FUNDO);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(criarTopo(),   BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout(0, 14));
        topo.setOpaque(false);

        JLabel titulo = new JLabel("Módulo: Adoção - Listagem de Adotantes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 40));

        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setOpaque(false);

        JTextField campo = new JTextField("Buscar adotante...") {
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
                if (campo.getText().equals("Buscar adotante...")) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText("Buscar adotante...");
                    campo.setForeground(CINZA_TEXTO);
                }
            }
        });

        // Filtro em tempo real enquanto digita
        campo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { aplicarFiltro(campo.getText()); }
            public void removeUpdate(DocumentEvent e)  { aplicarFiltro(campo.getText()); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltro(campo.getText()); }
        });

        JButton btnCadastrar = criarBotaoVerde("+ Cadastrar Adotante");
        btnCadastrar.addActionListener(e -> {
            Window janelaPai = SwingUtilities.getWindowAncestor(this);
            new DialogCadastroAdotante(janelaPai, this::atualizarTabela).setVisible(true);
        });

        barra.add(campo,        BorderLayout.CENTER);
        barra.add(btnCadastrar, BorderLayout.EAST);

        topo.add(titulo, BorderLayout.NORTH);
        topo.add(barra,  BorderLayout.CENTER);
        topo.setBorder(new EmptyBorder(0, 0, 16, 0));
        return topo;
    }

    // Aplica o filtro na tabela
    private void aplicarFiltro(String texto) {
        if (tabela == null || modelo == null) return;
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        if (texto.isEmpty() || texto.equals("Buscar adotante...")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private JPanel criarTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO);
        painel.setBorder(new LineBorder(CINZA_BORDA, 1, true));

        String[] colunas = {"Nome", "CPF", "Telefone", "Email", "Qtd. Adoções", "Ações"};
        java.util.List<data.Adotante> lista = utils.SistemaDados.adotantes;
        Object[][] dados = new Object[lista.size()][6];
        for (int i = 0; i < lista.size(); i++) {
            data.Adotante a = lista.get(i);
            dados[i][0] = a.getNome();
            dados[i][1] = a.getCpf();
            dados[i][2] = a.getTelefone();
            dados[i][3] = a.getEmail();
            dados[i][4] = a.getQuantidadeAdocoes();
            dados[i][5] = "";
        }

        // Usando os atributos da classe
        modelo = new DefaultTableModel(dados, colunas) {
            @Override public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(48);
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

        int[] larguras = {180, 150, 150, 180, 100, 220};
        for (int i = 0; i < larguras.length; i++)
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);

        // Coluna Qtd. Adoções — verde se > 0, cinza se 0
        tabela.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, row, col) -> {
            JLabel l = new JLabel(v.toString(), SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 14));
            int qtd = Integer.parseInt(v.toString());
            l.setForeground(qtd > 0 ? VERDE : CINZA_TEXTO);
            l.setOpaque(true);
            l.setBackground(sel ? new Color(232, 245, 233) : BRANCO);
            return l;
        });

        // Coluna Ações — botões editar e deletar
        tabela.getColumnModel().getColumn(5).setCellRenderer(new AcoesCellRenderer());
        tabela.getColumnModel().getColumn(5).setCellEditor(new AcoesCellEditor(tabela, modelo));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BRANCO);

        painel.add(scroll,           BorderLayout.CENTER);
        painel.add(criarPaginacao(), BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPaginacao() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(BRANCO);
        rodape.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, CINZA_BORDA),
            new EmptyBorder(8, 16, 8, 16)
        ));

        lblInfoPaginacao = new JLabel("Mostrando 1 a " + utils.SistemaDados.adotantes.size() + " de " + utils.SistemaDados.adotantes.size() + " adotantes");
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
        private final JPanel  painel     = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();

        public AcoesCellRenderer() {
            painel.setOpaque(true);
            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            painel.setBackground(sel ? new Color(232, 245, 233) : Color.WHITE);
            return painel;
        }
    }

    public void atualizarTabela() {
        if (modelo == null) return;
        modelo.setRowCount(0);
        java.util.List<data.Adotante> lista = utils.SistemaDados.adotantes;
        for (data.Adotante a : lista) {
            modelo.addRow(new Object[]{
                a.getNome(),
                a.getCpf(),
                a.getTelefone(),
                a.getEmail(),
                a.getQuantidadeAdocoes(),
                ""
            });
        }
        if (lblInfoPaginacao != null) {
            int total = lista.size();
            lblInfoPaginacao.setText("Mostrando 1 a " + total + " de " + total + " adotantes");
        }
    }

    // ── EDITOR DAS AÇÕES ─────────────────────────────────────────────────────
    private class AcoesCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel  painel     = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();
        private int linhaSelecionada;

        public AcoesCellEditor(JTable tabela, DefaultTableModel modelo) {
            painel.setOpaque(true);
            painel.setBackground(new Color(232, 245, 233));

            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                Window janelaPai = SwingUtilities.getWindowAncestor(tabela);
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Adotante adotante = utils.SistemaDados.adotantes.get(modelRow);
                new DialogEditarAdotante(janelaPai, adotante, PainelAdotante.this::atualizarTabela).setVisible(true);
            });

            btnDeletar.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Adotante adotante = utils.SistemaDados.adotantes.get(modelRow);
                int ok = JOptionPane.showConfirmDialog(tabela,
                    "Deseja deletar \"" + adotante.getNome() + "\"?\n(Todas as adoções dele serão excluídas)",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (ok == JOptionPane.YES_OPTION) {
                    java.util.List<data.Adoacao> aRemover = new java.util.ArrayList<>();
                    for (data.Adoacao adocao : utils.SistemaDados.adocoes) {
                        if (adocao.getAdotante() != null && adocao.getAdotante().getCpf().equals(adotante.getCpf())) {
                            aRemover.add(adocao);
                            if (adocao.getAnimal() != null) {
                                adocao.getAnimal().setAdotado(false);
                            }
                        }
                    }
                    utils.SistemaDados.adocoes.removeAll(aRemover);
                    utils.SistemaDados.adotantes.remove(adotante);
                    utils.SistemaDados.save();
                    atualizarTabela();
                }
            });

            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        @Override public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            linhaSelecionada = row;
            return painel;
        }

        @Override public Object getCellEditorValue() { return ""; }
    }
}