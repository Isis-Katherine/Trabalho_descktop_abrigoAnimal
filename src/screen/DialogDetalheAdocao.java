package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import data.Adoacao;

public class DialogDetalheAdocao extends JDialog {

    private static final Color VERDE       = new Color(46, 125, 50);
    private static final Color VERDE_HOVER = new Color(27, 94, 32);
    private static final Color VERDE_TEXTO = new Color(27, 94, 32);
    private static final Color VERDE_CLARO = new Color(200, 230, 201);
    private static final Color LARANJA     = new Color(230, 81, 0);
    private static final Color LARANJA_CLARO = new Color(255, 224, 178);
    private static final Color FUNDO       = new Color(248, 250, 248);
    private static final Color BRANCO      = Color.WHITE;
    private static final Color CINZA_BORDA = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO = new Color(100, 100, 100);

    public DialogDetalheAdocao(Window pai, Adoacao adocao) {
        super(pai, "Detalhes da Adoção - " + adocao.getId(), ModalityType.APPLICATION_MODAL);
        setSize(800, 560);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(adocao), BorderLayout.NORTH);
        add(criarCorpo(adocao),     BorderLayout.CENTER);
        add(criarRodape(),         BorderLayout.SOUTH);
    }

    // ── CABEÇALHO ─────────────────────────────────────────────────────────────
    private JPanel criarCabecalho(Adoacao adocao) {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(VERDE);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JPanel esq = new JPanel();
        esq.setLayout(new BoxLayout(esq, BoxLayout.Y_AXIS));
        esq.setOpaque(false);

        JLabel titulo = new JLabel("Detalhes da Adoção");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataStr = adocao.getDataAdoacao() != null ? sdf.format(adocao.getDataAdoacao()) : "N/D";

        JLabel sub = new JLabel("ID: " + adocao.getId() + "  •  Data: " + dataStr);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 230, 200));

        esq.add(titulo);
        esq.add(Box.createVerticalStrut(4));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        esq.add(sub);

        // Badge situação
        boolean devolvida = Boolean.TRUE.equals(adocao.getDevolucao());
        String sit = devolvida ? "Devolvido" : "Concluída";

        JLabel badge = new JLabel("  " + sit + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(!devolvida ? VERDE_TEXTO : LARANJA);
        badge.setBackground(!devolvida ? VERDE_CLARO : LARANJA_CLARO);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));

        cab.add(esq,   BorderLayout.CENTER);
        cab.add(badge, BorderLayout.EAST);
        return cab;
    }

    // ── CORPO ─────────────────────────────────────────────────────────────────
    private JPanel criarCorpo(Adoacao adocao) {
        JPanel corpo = new JPanel(new GridLayout(1, 3, 16, 0));
        corpo.setBackground(FUNDO);
        corpo.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Card 1: dados do adotante
        JPanel cardAdotante = criarCard("👤 Adotante");
        if (adocao.getAdotante() != null) {
            cardAdotante.add(criarLinha("Nome:",     adocao.getAdotante().getNome()));
            cardAdotante.add(criarLinha("Telefone:", adocao.getAdotante().getTelefone()));
            cardAdotante.add(criarLinha("E-mail:",   adocao.getAdotante().getEmail()));
            cardAdotante.add(criarLinha("CPF:",      adocao.getAdotante().getCpf()));
            cardAdotante.add(criarLinha("CEP:",      adocao.getAdotante().getCep()));
        } else {
            cardAdotante.add(criarLinha("Aviso:", "Sem adotante vinculado"));
        }
        cardAdotante.add(Box.createVerticalGlue());

        // Card 2: dados do animal
        JPanel cardAnimal = criarCard("🐾 Animal");

        // Avatar do animal
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(210, 180, 140));
                g2.fillOval(0, 0, 60, 60);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
                g2.drawString("🐶", 12, 46);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(60, 60));
        avatar.setMaximumSize(new Dimension(60, 60));
        avatar.setOpaque(false);
        avatar.setAlignmentX(LEFT_ALIGNMENT);
        cardAnimal.add(avatar);
        cardAnimal.add(Box.createVerticalStrut(8));

        if (adocao.getAnimal() != null) {
            cardAnimal.add(criarLinha("Nome:",           adocao.getAnimal().getNome()));
            cardAnimal.add(criarLinha("Espécie:",        adocao.getAnimal().geEspecie()));
            cardAnimal.add(criarLinha("Raça:",           adocao.getAnimal().getRaca()));
            cardAnimal.add(criarLinha("Porte:",          adocao.getAnimal().getPorte()));
            cardAnimal.add(criarLinha("Status:",         adocao.getAnimal().getAdotado() ? "Adotado" : "Disponível"));
        } else {
            cardAnimal.add(criarLinha("Aviso:", "Sem animal vinculado"));
        }
        cardAnimal.add(Box.createVerticalGlue());

        // Card 3: dados da adoção
        JPanel cardAdocao = criarCard("📋 Adoção");
        cardAdocao.add(criarLinha("Responsável:",  adocao.getResponsavel() != null ? adocao.getResponsavel() : "Ana Costa"));
        cardAdocao.add(criarLinha("Comprovante:",  adocao.getComprovante() != null ? adocao.getComprovante() : "N/D"));
        
        if (Boolean.TRUE.equals(adocao.getDevolucao())) {
            java.text.SimpleDateFormat sdfSimple = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String devStr = adocao.getDataDevolucao() != null ? sdfSimple.format(adocao.getDataDevolucao()) : "N/D";
            cardAdocao.add(criarLinha("Devolvido em:", devStr));
            cardAdocao.add(criarLinha("Motivo:", adocao.getMotivoDevolucao() != null ? adocao.getMotivoDevolucao() : ""));
        }

        cardAdocao.add(Box.createVerticalStrut(8));

        JLabel lblObs = new JLabel("Observações:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblObs.setForeground(CINZA_TEXTO);
        lblObs.setAlignmentX(LEFT_ALIGNMENT);
        cardAdocao.add(lblObs);
        cardAdocao.add(Box.createVerticalStrut(4));

        String obsText = adocao.getObservacoes();
        if (obsText == null || obsText.trim().isEmpty()) {
            obsText = "Sem observações registradas.";
        }
        JTextArea obs = new JTextArea(obsText);
        obs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        obs.setForeground(new Color(60, 60, 60));
        obs.setBackground(new Color(245, 245, 245));
        obs.setLineWrap(true);
        obs.setWrapStyleWord(true);
        obs.setEditable(false);
        obs.setBorder(new EmptyBorder(8, 10, 8, 10));
        obs.setAlignmentX(LEFT_ALIGNMENT);
        cardAdocao.add(obs);
        cardAdocao.add(Box.createVerticalStrut(12));

        JButton btnComp = new JButton("📄 Visualizar Comprovante");
        btnComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnComp.setBackground(new Color(238, 238, 238));
        btnComp.setForeground(new Color(60, 60, 60));
        btnComp.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        btnComp.setFocusPainted(false);
        btnComp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnComp.setAlignmentX(LEFT_ALIGNMENT);
        btnComp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cardAdocao.add(btnComp);
        cardAdocao.add(Box.createVerticalStrut(8));

        JButton btnImprimir = new JButton("🖨 Imprimir Ficha");
        btnImprimir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnImprimir.setBackground(new Color(238, 238, 238));
        btnImprimir.setForeground(new Color(60, 60, 60));
        btnImprimir.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        btnImprimir.setFocusPainted(false);
        btnImprimir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImprimir.setAlignmentX(LEFT_ALIGNMENT);
        btnImprimir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cardAdocao.add(btnImprimir);
        cardAdocao.add(Box.createVerticalGlue());

        corpo.add(cardAdotante);
        corpo.add(cardAnimal);
        corpo.add(cardAdocao);
        return corpo;
    }

    // ── RODAPÉ ────────────────────────────────────────────────────────────────
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        rodape.setBackground(FUNDO);
        rodape.setBorder(new MatteBorder(1, 0, 0, 0, CINZA_BORDA));

        JButton btnFechar = new JButton("Fechar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFechar.setForeground(BRANCO);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setFocusPainted(false);
        btnFechar.setBorder(new EmptyBorder(10, 30, 10, 30));
        btnFechar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());

        rodape.add(btnFechar);
        return rodape;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────
    private JPanel criarCard(String titulo) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BRANCO);
        card.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(VERDE);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(lbl);

        return card;
    }

    private JPanel criarLinha(String rotulo, String valor) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setOpaque(false);
        linha.setAlignmentX(LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        linha.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(CINZA_TEXTO);
        lbl.setPreferredSize(new Dimension(100, 20));

        JLabel val = new JLabel(valor);
        val.setFont(new Font("Segoe UI", Font.BOLD, 12));
        val.setForeground(new Color(40, 40, 40));

        linha.add(lbl, BorderLayout.WEST);
        linha.add(val, BorderLayout.CENTER);
        return linha;
    }
}