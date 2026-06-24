package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import data.Adotante;

public class DialogEditarAdotante extends JDialog {

    private static final Color AMARELO        = new Color(251, 192, 45);
    private static final Color AMARELO_HOVER  = new Color(249, 168, 37);
    private static final Color FUNDO          = new Color(248, 250, 248);
    private static final Color BRANCO         = Color.WHITE;
    private static final Color CINZA_BORDA    = new Color(220, 220, 220);

    private Adotante adotante;
    private Runnable refreshCallback;
    private JTextField campoNome;
    private JTextField campoCPF;
    private JTextField campoTelefone;
    private JTextField campoEmail;

    public DialogEditarAdotante(
            Window pai,
            Adotante adotante,
            Runnable refreshCallback
    ) {

        super(pai, "Editar Adotante", ModalityType.APPLICATION_MODAL);
        this.adotante = adotante;
        this.refreshCallback = refreshCallback;

        setSize(480, 550);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);
    }

    // CABEÇALHO
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(AMARELO);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Editar Adotante");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Altere os dados do adotante");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 248, 225));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(sub);

        cab.add(textos, BorderLayout.CENTER);
        return cab;
    }

    // FORMULÁRIO
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        campoNome = criarCampoTextoComponente(adotante.getNome());
        campoCPF = criarCampoTextoComponente(adotante.getCpf());
        campoTelefone = criarCampoTextoComponente(adotante.getTelefone());
        campoEmail = criarCampoTextoComponente(adotante.getEmail());

        form.add(criarGrupo("Nome completo *", campoNome));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("CPF *", campoCPF));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Telefone *", campoTelefone));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Email *", campoEmail));

        return form;
    }

    private JTextField criarCampoTextoComponente(String valor) {
        JTextField campo = new JTextField(valor) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    10,
                    10
                ));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    10,
                    10
                ));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(new EmptyBorder(10, 12, 10, 12));
        campo.setOpaque(false);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return campo;
    }

    private JPanel criarGrupo(String rotulo, JTextField campo) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campo);

        return grupo;
    }

    // BOTÕES
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        painel.setBackground(FUNDO);
        painel.setBorder(new MatteBorder(
            1,
            0,
            0,
            0,
            CINZA_BORDA
        ));

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

        JButton btnSalvar = new JButton("Salvar Alterações") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(
                    getModel().isRollover()
                    ? AMARELO_HOVER
                    : AMARELO
                );
                g2.fill(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    10,
                    10
                ));
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
            String cpf = campoCPF.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Telefone é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            adotante.setNome(nome);
            adotante.setCPF(cpf);
            adotante.setTelefone(telefone);
            adotante.setEmail(email);

            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(
                this,
                "Dados alterados com sucesso!"
            );

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
