package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import data.Adotante;

public class DialogCadastroAdotante extends JDialog {

    private static final Color VERDE       = new Color(46, 125, 50);
    private static final Color VERDE_HOVER = new Color(27, 94, 32);
    private static final Color FUNDO       = new Color(248, 250, 248);
    private static final Color BRANCO      = Color.WHITE;
    private static final Color CINZA_BORDA = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO = new Color(150, 150, 150);

    private JTextField campoNome;
    private JTextField campoCPF;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private Runnable refreshCallback;

    public DialogCadastroAdotante(Window pai, Runnable refreshCallback) {
        super(pai, "Cadastrar Adotante", ModalityType.APPLICATION_MODAL);
        this.refreshCallback = refreshCallback;
        setSize(480, 550);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(),  BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(),     BorderLayout.SOUTH);
    }

    // ── CABEÇALHO ─────────────────────────────────────────────────────────────
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(VERDE);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Cadastrar Adotante");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Preencha os dados do adotante");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 230, 200));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(sub);

        cab.add(textos, BorderLayout.CENTER);
        return cab;
    }

    // ── FORMULÁRIO ────────────────────────────────────────────────────────────
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        JPanel gpNome = criarCampoTextoGrupo("Nome completo *", "Ex: Ana Paula Souza");
        campoNome = (JTextField) gpNome.getClientProperty("field");
        
        JPanel gpCPF = criarCampoCPF();
        
        JPanel gpTelefone = criarCampoTelefone();
        
        JPanel gpEmail = criarCampoTextoGrupo("Email *", "Ex: ana@email.com");
        campoEmail = (JTextField) gpEmail.getClientProperty("field");

        form.add(gpNome);
        form.add(Box.createVerticalStrut(14));
        form.add(gpCPF);
        form.add(Box.createVerticalStrut(14));
        form.add(gpTelefone);
        form.add(Box.createVerticalStrut(14));
        form.add(gpEmail);

        return form;
    }

    private JPanel criarCampoTextoGrupo(String rotulo, String placeholder) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JTextField campo = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(CINZA_TEXTO);
        campo.setBorder(new EmptyBorder(10, 12, 10, 12));
        campo.setOpaque(false);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText(""); 
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder); campo.setForeground(CINZA_TEXTO);
                }
            }
        });

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campo);
        grupo.putClientProperty("field", campo);
        return grupo;
    }

    private JPanel criarCampoCPF() {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("CPF *");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        campoCPF = new JTextField("000.000.000-00") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campoCPF.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoCPF.setForeground(CINZA_TEXTO);
        campoCPF.setBorder(new EmptyBorder(10, 12, 10, 12));
        campoCPF.setOpaque(false);
        campoCPF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campoCPF.setAlignmentX(LEFT_ALIGNMENT);

        campoCPF.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoCPF.getText().equals("000.000.000-00")) {
                    campoCPF.setText(""); campoCPF.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoCPF.getText().isEmpty()) {
                    campoCPF.setText("000.000.000-00"); campoCPF.setForeground(CINZA_TEXTO);
                }
            }
        });

        campoCPF.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) { e.consume(); return; }
                String soNum = campoCPF.getText().replaceAll("[^0-9]", "");
                if (soNum.length() >= 11) { e.consume(); return; }
                SwingUtilities.invokeLater(() -> {
                    String n = campoCPF.getText().replaceAll("[^0-9]", "");
                    StringBuilder f = new StringBuilder();
                    for (int i = 0; i < n.length(); i++) {
                        if (i == 3 || i == 6) f.append(".");
                        if (i == 9) f.append("-");
                        f.append(n.charAt(i));
                    }
                    campoCPF.setText(f.toString());
                    campoCPF.setCaretPosition(campoCPF.getText().length());
                });
            }
        });

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campoCPF);
        return grupo;
    }

    private JPanel criarCampoTelefone() {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Telefone *");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        campoTelefone = new JTextField("(00) 00000-0000") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campoTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoTelefone.setForeground(CINZA_TEXTO);
        campoTelefone.setBorder(new EmptyBorder(10, 12, 10, 12));
        campoTelefone.setOpaque(false);
        campoTelefone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campoTelefone.setAlignmentX(LEFT_ALIGNMENT);

        campoTelefone.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoTelefone.getText().equals("(00) 00000-0000")) {
                    campoTelefone.setText(""); campoTelefone.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (campoTelefone.getText().isEmpty()) {
                    campoTelefone.setText("(00) 00000-0000"); campoTelefone.setForeground(CINZA_TEXTO);
                }
            }
        });

        campoTelefone.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) { e.consume(); return; }
                String soNum = campoTelefone.getText().replaceAll("[^0-9]", "");
                if (soNum.length() >= 11) { e.consume(); return; }
                SwingUtilities.invokeLater(() -> {
                    String n = campoTelefone.getText().replaceAll("[^0-9]", "");
                    StringBuilder f = new StringBuilder();
                    for (int i = 0; i < n.length(); i++) {
                        if (i == 0) f.append("(");
                        if (i == 2) f.append(") ");
                        if (i == 7) f.append("-");
                        f.append(n.charAt(i));
                    }
                    campoTelefone.setText(f.toString());
                    campoTelefone.setCaretPosition(campoTelefone.getText().length());
                });
            }
        });

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campoTelefone);
        return grupo;
    }

    // ── BOTÕES ────────────────────────────────────────────────────────────────
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        painel.setBackground(FUNDO);
        painel.setBorder(new MatteBorder(1, 0, 0, 0, CINZA_BORDA));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(238, 238, 238));
        btnCancelar.setForeground(new Color(60, 60, 60));
        btnCancelar.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar Adotante") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setForeground(BRANCO);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty() || nome.equals("Ex: Ana Paula Souza")) {
                JOptionPane.showMessageDialog(this, "Nome completo é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String cpf = campoCPF.getText().trim();
            if (cpf.isEmpty() || cpf.equals("000.000.000-00")) {
                JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String telefone = campoTelefone.getText().trim();
            if (telefone.isEmpty() || telefone.equals("(00) 00000-0000")) {
                JOptionPane.showMessageDialog(this, "Telefone é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String email = campoEmail.getText().trim();
            if (email.isEmpty() || email.equals("Ex: ana@email.com")) {
                JOptionPane.showMessageDialog(this, "Email é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Adotante adotante = new Adotante(
                nome,
                new Date(),
                cpf,
                telefone,
                email,
                "00000-000"
            );

            utils.SistemaDados.adotantes.add(adotante);
            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(this,
                "Adotante cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (refreshCallback != null) {
                refreshCallback.run();
            }
            dispose();
        });

        painel.add(btnCancelar);
        painel.add(btnSalvar);
        return painel;
    }
}