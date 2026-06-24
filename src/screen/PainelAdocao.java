package screen;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import utils.BotaoEditar;
import utils.BotaoDeletar;

public class PainelAdocao extends JPanel {

    private static final Color VERDE         = new Color(46, 125, 50);
    private static final Color VERDE_HOVER   = new Color(27, 94, 32);
    private static final Color VERDE_CLARO   = new Color(232, 245, 233);
    private static final Color VERDE_TEXTO   = new Color(27, 94, 32);
    private static final Color FUNDO         = new Color(248, 250, 248);
    private static final Color BRANCO        = Color.WHITE;
    private static final Color CINZA_BORDA   = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO   = new Color(100, 100, 100);
    private static final Color LARANJA       = new Color(230, 81, 0);
    private static final Color LARANJA_CLARO = new Color(255, 224, 178);

    // Atributos da tabela e modelo
    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel lblInfoPaginacao;

    // Atributos dos campos de filtro
    private JTextField campoAdotante;
    private JTextField campoAnimal;
    private JComboBox<String> comboSituacao;
    private JTextField campoDataInicial ; 
    private JTextField campoDataFinal;

    public PainelAdocao() {
        setLayout(new BorderLayout());
        setBackground(FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel tudo = new JPanel();
        tudo.setLayout(new BoxLayout(tudo, BoxLayout.Y_AXIS));
        tudo.setOpaque(false);
        tudo.add(criarTopo());
        tudo.add(Box.createVerticalStrut(12));
        tudo.add(criarFiltros());
        tudo.add(Box.createVerticalStrut(12));
        tudo.add(criarTabela());
        tudo.add(Box.createVerticalStrut(12));
        tudo.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(tudo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ── TOPO ─────────────────────────────────────────────────────────────────
    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JPanel esq = new JPanel();
        esq.setLayout(new BoxLayout(esq, BoxLayout.Y_AXIS));
        esq.setOpaque(false);

        JLabel titulo = new JLabel("Módulo: Adoção - Listagem de Adoções");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(30, 30, 30));

        JLabel sub = new JLabel("Visualize e verifique todas as adoções realizadas pelo abrigo.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(CINZA_TEXTO);

        esq.add(titulo);
        esq.add(Box.createVerticalStrut(4));
        esq.add(sub);

        JButton btnNova = criarBotaoVerde("+ Nova Adoção");
        btnNova.addActionListener(e -> {
            Window janelaPai = SwingUtilities.getWindowAncestor(this);
            new DialogCadastroAdoacao(janelaPai, this::atualizarTabela).setVisible(true);
        });

        topo.add(esq,     BorderLayout.CENTER);
        topo.add(btnNova, BorderLayout.EAST);
        return topo;
    }

    // ── FILTROS ──────────────────────────────────────────────────────────────
    private JPanel criarFiltros() {
        JPanel card = criarCard();
        card.setLayout(new BorderLayout(0, 10));

        JLabel lblFiltros = new JLabel("⚡ Filtros");
        lblFiltros.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFiltros.setForeground(VERDE);
        lblFiltros.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel campos = new JPanel(new GridLayout(1, 6, 10, 0));
        campos.setOpaque(false);

        // Cria e guarda os campos como atributos da classe
        campoDataInicial = criarCampoDataFiltro("dd/mm/aaaa");
        campoDataFinal   = criarCampoDataFiltro("dd/mm/aaaa");

        campoAdotante = criarCampoBuscaSimples("Buscar adotante...");
        campoAnimal   = criarCampoBuscaSimples("Buscar animal...");
        comboSituacao = new JComboBox<>(new String[]{"Todas", "Concluída", "Pendente", "Cancelada"});
        comboSituacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboSituacao.setBackground(BRANCO);

        campos.add(criarGrupoFiltro("Data Inicial", campoDataInicial));
        campos.add(criarGrupoFiltro("Data Final",   campoDataFinal));
        campos.add(criarGrupoFiltro("Adotante",     criarCampoBuscaFiltroComCampo(campoAdotante)));
        campos.add(criarGrupoFiltro("Animal",       criarCampoBuscaFiltroComCampo(campoAnimal)));
        campos.add(criarGrupoFiltro("Situação",     comboSituacao));

        JPanel botoes = new JPanel();
        botoes.setLayout(new BoxLayout(botoes, BoxLayout.Y_AXIS));
        botoes.setOpaque(false);

        JButton btnFiltrar = criarBotaoVerde("🔍 Filtrar");
        btnFiltrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFiltrar.setMaximumSize(new Dimension(140, 36));
        btnFiltrar.addActionListener(e -> aplicarFiltros());

        JButton btnLimpar = new JButton("🗑 Limpar Filtros");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimpar.setForeground(CINZA_TEXTO);
        btnLimpar.setBackground(new Color(238, 238, 238));
        btnLimpar.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpar.setMaximumSize(new Dimension(140, 36));
        btnLimpar.addActionListener(e -> limparFiltros());

        botoes.add(Box.createVerticalGlue());
        botoes.add(btnFiltrar);
        botoes.add(Box.createVerticalStrut(6));
        botoes.add(btnLimpar);
        botoes.add(Box.createVerticalGlue());

        campos.add(botoes);

        card.add(lblFiltros, BorderLayout.NORTH);
        card.add(campos,     BorderLayout.CENTER);
        return card;
    }

    // Aplica todos os filtros de uma vez ao clicar em Filtrar
    private void aplicarFiltros() {
        if (tabela == null || modelo == null) return;

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        // Filtro por adotante (coluna 1)
        String adotante = campoAdotante.getText().trim();
        if (!adotante.isEmpty() && !adotante.equals("Buscar adotante..."))
            filtros.add(RowFilter.regexFilter("(?i)" + adotante, 1));

        // Filtro por animal (coluna 2)
        String animal = campoAnimal.getText().trim();
        if (!animal.isEmpty() && !animal.equals("Buscar animal..."))
            filtros.add(RowFilter.regexFilter("(?i)" + animal, 2));

        // Filtro por Datas

        String dataInicial = campoDataInicial.getText().trim();
        String dataFinal   = campoDataFinal.getText().trim();

        if (!dataInicial.isEmpty() && !dataInicial.equals("dd/mm/aaaa"))
            filtros.add(RowFilter.regexFilter("(?i)" + dataInicial, 0)); // coluna Data da Adoção

        if (!dataFinal.isEmpty() && !dataFinal.equals("dd/mm/aaaa"))
            filtros.add(RowFilter.regexFilter("(?i)" + dataFinal, 6));

        // Filtro por situação (coluna 5)
        String situacao = comboSituacao.getSelectedItem().toString();
        if (!situacao.equals("Todas"))
            filtros.add(RowFilter.regexFilter("(?i)" + situacao, 5));

        if (filtros.isEmpty())
            sorter.setRowFilter(null);
        else
            sorter.setRowFilter(RowFilter.andFilter(filtros));
    }

    // Limpa todos os filtros
    private void limparFiltros() {
        campoAdotante.setText("Buscar adotante...");
        campoAdotante.setForeground(CINZA_TEXTO);
        campoAnimal.setText("Buscar animal...");
        campoAnimal.setForeground(CINZA_TEXTO);
        comboSituacao.setSelectedIndex(0);
        campoDataInicial.setText("dd/mm/aaaa");
        campoDataInicial.setForeground(CINZA_TEXTO);
        campoDataFinal.setText("dd/mm/aaaa");
        campoDataFinal.setForeground(CINZA_TEXTO);
        if (tabela != null) tabela.setRowSorter(null);
    }

    private JPanel criarGrupoFiltro(String rotulo, JComponent campo) {
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        g.setOpaque(false);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(80, 80, 80));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        campo.setAlignmentX(LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(campo);
        return g;
    }

    private JTextField criarCampoDataFiltro(String placeholder) {
        JTextField campo = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setForeground(CINZA_TEXTO);
        campo.setBorder(new EmptyBorder(8, 10, 8, 10));
        campo.setOpaque(false);
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) { campo.setText(placeholder); campo.setForeground(CINZA_TEXTO); }
            }
        });
        campo.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) { e.consume(); return; }
                if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(Color.BLACK); }
                if (campo.getText().replace("/", "").length() >= 8) { e.consume(); return; }
                SwingUtilities.invokeLater(() -> {
                    String n = campo.getText().replace("/", "");
                    StringBuilder f = new StringBuilder();
                    for (int i = 0; i < n.length(); i++) {
                        if (i == 2 || i == 4) f.append("/");
                        f.append(n.charAt(i));
                    }
                    campo.setText(f.toString());
                    campo.setCaretPosition(campo.getText().length());
                });
            }
        });
        return campo;
    }

    // Campo de texto simples para busca (sem ícone)
    private JTextField criarCampoBuscaSimples(String placeholder) {
        JTextField campo = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setForeground(CINZA_TEXTO);
        campo.setBorder(new EmptyBorder(8, 10, 8, 10));
        campo.setOpaque(false);
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) { campo.setText(placeholder); campo.setForeground(CINZA_TEXTO); }
            }
        });
        return campo;
    }

    // Painel com campo de busca + ícone de lupa
    private JPanel criarCampoBuscaFiltroComCampo(JTextField campo) {
        JPanel painel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
            }
        };
        painel.setOpaque(false);
        campo.setBorder(new EmptyBorder(8, 10, 8, 4));

        JLabel icone = new JLabel("🔍");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        icone.setBorder(new EmptyBorder(0, 2, 0, 8));

        painel.add(campo, BorderLayout.CENTER);
        painel.add(icone, BorderLayout.EAST);
        return painel;
    }

    // ── TABELA ───────────────────────────────────────────────────────────────
    private JPanel criarTabela() {
        JPanel card = criarCard();
        card.setLayout(new BorderLayout(0, 10));

        JLabel lblLista = new JLabel("Lista de Adoções");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLista.setForeground(VERDE);
        lblLista.setBorder(new EmptyBorder(0, 0, 8, 0));

        String[] colunas = {"Data da Adoção", "Adotante", "Animal", "Espécie", "Raça", "Situação","Data Devolução", "Responsável", "Comprovante", "Ações"};

        java.util.List<data.Adoacao> lista = utils.SistemaDados.adocoes;
        Object[][] dados = new Object[lista.size()][10];
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.text.SimpleDateFormat sdfSimple = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < lista.size(); i++) {
            data.Adoacao ad = lista.get(i);
            dados[i][0] = ad.getDataAdoacao() != null ? sdf.format(ad.getDataAdoacao()) : "";
            dados[i][1] = ad.getAdotante() != null ? ad.getAdotante().getNome() : "";
            dados[i][2] = ad.getAnimal() != null ? ad.getAnimal().getNome() : "";
            dados[i][3] = ad.getAnimal() != null ? ad.getAnimal().geEspecie() : "";
            dados[i][4] = ad.getAnimal() != null ? ad.getAnimal().getRaca() : "";
            dados[i][5] = Boolean.TRUE.equals(ad.getDevolucao()) ? "Devolvido" : "Concluída";
            dados[i][6] = ad.getDataDevolucao() != null ? sdfSimple.format(ad.getDataDevolucao()) : "";
            dados[i][7] = ad.getResponsavel() != null ? ad.getResponsavel() : "";
            dados[i][8] = ad.getComprovante() != null ? ad.getComprovante() : "";
            dados[i][9] = "";
        }

        modelo = new DefaultTableModel(dados, colunas) {
            @Override public boolean isCellEditable(int r, int c) { return c == 9; }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(52);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setGridColor(new Color(240, 240, 240));
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(VERDE_CLARO);
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFillsViewportHeight(true);

        JTableHeader cab = tabela.getTableHeader();
        cab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cab.setBackground(new Color(248, 248, 248));
        cab.setForeground(new Color(60, 60, 60));
        cab.setBorder(new MatteBorder(0, 0, 1, 0, CINZA_BORDA));
        cab.setPreferredSize(new Dimension(0, 40));

        int[] larguras = {120, 120, 70, 70, 70, 80, 90, 90, 70, 160}; 
        for (int i = 0; i < larguras.length; i++)
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);

        // Coluna Situação → badge
        tabela.getColumnModel().getColumn(5).setCellRenderer((t, v, sel, foc, row, col) -> {
            boolean concluida = "Concluída".equals(String.valueOf(v));
            boolean pendente  = "Pendente".equals(String.valueOf(v));
            JPanel badge = new JPanel(new GridBagLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bg = concluida ? new Color(200, 230, 201) : pendente ? LARANJA_CLARO : new Color(220, 220, 220);
                    g2.setColor(bg);
                    g2.fillRoundRect(6, 12, getWidth() - 12, getHeight() - 24, 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setBackground(sel ? VERDE_CLARO : BRANCO);
            JLabel l = new JLabel(String.valueOf(v));
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            l.setForeground(concluida ? VERDE_TEXTO : pendente ? LARANJA : CINZA_TEXTO);
            badge.add(l);
            return badge;
        });

        // Coluna Comprovante → ícone
        tabela.getColumnModel().getColumn(8).setCellRenderer((t, v, sel, foc, row, col) -> {
            JLabel l = new JLabel("📄", SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            l.setOpaque(true);
            l.setBackground(sel ? VERDE_CLARO : BRANCO);
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return l;
        });

        // Coluna Ações
        tabela.getColumnModel().getColumn(9).setCellRenderer(new AcoesCellRenderer());
        tabela.getColumnModel().getColumn(9).setCellEditor(new AcoesCellEditor(tabela, modelo));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BRANCO);

        card.add(lblLista,         BorderLayout.NORTH);
        card.add(scroll,           BorderLayout.CENTER);
        card.add(criarPaginacao(), BorderLayout.SOUTH);
        return card;
    }

    // ── PAGINAÇÃO ─────────────────────────────────────────────────────────────
    private JPanel criarPaginacao() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(BRANCO);
        rodape.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, CINZA_BORDA),
            new EmptyBorder(8, 0, 0, 0)
        ));

        lblInfoPaginacao = new JLabel("Mostrando 1 a " + utils.SistemaDados.adocoes.size() + " de " + utils.SistemaDados.adocoes.size() + " adoções");
        lblInfoPaginacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfoPaginacao.setForeground(CINZA_TEXTO);

        JPanel pags = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pags.setOpaque(false);
        String[] paginas = {"1", "2", "3", "4", "5", "...", "8", "›"};
        for (int i = 0; i < paginas.length; i++)
            pags.add(criarBtnPagina(paginas[i], i == 0));

        rodape.add(lblInfoPaginacao, BorderLayout.WEST);
        rodape.add(pags, BorderLayout.EAST);
        return rodape;
    }

    // ── RENDERER AÇÕES ───────────────────────────────────────────────────────
    private static class AcoesCellRenderer implements TableCellRenderer {
        private final JPanel  painel     = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 10));
        private final JButton btnVer     = new JButton("👁");
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();

        public AcoesCellRenderer() {
            painel.setOpaque(true);
            estilizar(btnVer, new Color(46, 125, 50));
            painel.add(btnVer);
            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        private void estilizar(JButton btn, Color cor) {
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
            btn.setForeground(Color.WHITE);
            btn.setBackground(cor);
            btn.setBorder(new EmptyBorder(5, 8, 5, 8));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
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
        java.util.List<data.Adoacao> lista = utils.SistemaDados.adocoes;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.text.SimpleDateFormat sdfSimple = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (data.Adoacao ad : lista) {
            modelo.addRow(new Object[]{
                ad.getDataAdoacao() != null ? sdf.format(ad.getDataAdoacao()) : "",
                ad.getAdotante() != null ? ad.getAdotante().getNome() : "",
                ad.getAnimal() != null ? ad.getAnimal().getNome() : "",
                ad.getAnimal() != null ? ad.getAnimal().geEspecie() : "",
                ad.getAnimal() != null ? ad.getAnimal().getRaca() : "",
                Boolean.TRUE.equals(ad.getDevolucao()) ? "Devolvido" : "Concluída",
                ad.getDataDevolucao() != null ? sdfSimple.format(ad.getDataDevolucao()) : "",
                ad.getResponsavel() != null ? ad.getResponsavel() : "",
                ad.getComprovante() != null ? ad.getComprovante() : "",
                ""
            });
        }
        if (lblInfoPaginacao != null) {
            int total = lista.size();
            lblInfoPaginacao.setText("Mostrando 1 a " + total + " de " + total + " adoções");
        }
    }

    // ── EDITOR AÇÕES ─────────────────────────────────────────────────────────
    private class AcoesCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel  painel     = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 10));
        private final JButton btnVer     = new JButton("👁");
        private final JButton btnEditar  = new BotaoEditar();
        private final JButton btnDeletar = new BotaoDeletar();
        private int linhaSelecionada;

        public AcoesCellEditor(JTable tabela, DefaultTableModel modelo) {
            painel.setOpaque(true);
            painel.setBackground(new Color(232, 245, 233));
            estilizar(btnVer, new Color(46, 125, 50));

            btnVer.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Adoacao adocao = utils.SistemaDados.adocoes.get(modelRow);
                Window janelaPai = SwingUtilities.getWindowAncestor(tabela);
                new DialogDetalheAdocao(janelaPai, adocao).setVisible(true);
            });

        btnEditar.addActionListener(e -> {

            System.out.println("CLICOU EM EDITAR");

            fireEditingStopped();

            Window janelaPai =
                SwingUtilities.getWindowAncestor(tabela);

            int modelRow =
                tabela.convertRowIndexToModel(linhaSelecionada);

            data.Adoacao adocao =
                utils.SistemaDados.adocoes.get(modelRow);

            new DialogEditarAdoacao(
                janelaPai,
                adocao,
                PainelAdocao.this::atualizarTabela
            ).setVisible(true);
        });

            btnDeletar.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                data.Adoacao adocao = utils.SistemaDados.adocoes.get(modelRow);
                int ok = JOptionPane.showConfirmDialog(tabela,
                    "Deseja deletar a adoção do animal \"" + (adocao.getAnimal() != null ? adocao.getAnimal().getNome() : "Sem nome") + "\"?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ok == JOptionPane.YES_OPTION) {
                    if (adocao.getAnimal() != null) {
                        adocao.getAnimal().setAdotado(false);
                    }

                    String id = adocao.getId();
                    utils.SistemaDados.adocoes.removeIf(
                        a -> a.getId().equals(id)
                    );          
                    utils.SistemaDados.save();
                    atualizarTabela();
                }
            });

            painel.add(btnVer);
            painel.add(btnEditar);
            painel.add(btnDeletar);
        }

        private void estilizar(JButton btn, Color cor) {
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
            btn.setForeground(Color.WHITE);
            btn.setBackground(cor);
            btn.setBorder(new EmptyBorder(5, 8, 5, 8));
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

    // ── HELPERS ──────────────────────────────────────────────────────────────
    private JPanel criarCard() {
        JPanel card = new JPanel();
        card.setBackground(BRANCO);
        card.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    private JButton criarBotaoVerde(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BRANCO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(ativo ? BRANCO : CINZA_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(34, 28));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}