package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import data.Animal;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DialogEditarAnimal extends JDialog {

    private static final Color AMARELO = new Color(251, 192, 45);
    private static final Color AMARELO_HOVER = new Color(249, 168, 37);

    private static final Color FUNDO = new Color(248, 250, 248);
    private static final Color BRANCO = Color.WHITE;

    private static final Color CINZA_BORDA = new Color(220, 220, 220);

    private JTextField campoNome;
    private JTextField campoRaca;

    private JComboBox<String> comboEspecie;
    private JComboBox<String> comboPorte;
    private JComboBox<String> comboSexo;
    private JComboBox<String> comboStatus;

    private Animal animal;
    private Runnable refreshCallback;

    private JLabel lblFoto;
    private String caminhoImagem;

    public DialogEditarAnimal(
            Window pai,
            Animal animal,
            Runnable refreshCallback
    ) {

        super(pai, "Editar Animal", ModalityType.APPLICATION_MODAL);
        this.animal = animal;
        this.refreshCallback = refreshCallback;

        setSize(480, 700);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(criarFormulario());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        add(criarBotoes(), BorderLayout.SOUTH);

        
        // ───── PREENCHER CAMPOS ─────
        campoNome.setText(animal.getNome());
        caminhoImagem = animal.getCaminhoImagem();

        if (caminhoImagem != null && !caminhoImagem.isEmpty()) {

        ImageIcon icon =
                new ImageIcon(caminhoImagem);

        Image imagem =
                icon.getImage().getScaledInstance(
                120,
                120,
                Image.SCALE_SMOOTH
                );

        lblFoto.setText("");
        lblFoto.setIcon(new ImageIcon(imagem));
        }

        campoRaca.setText(animal.getRaca() != null ? animal.getRaca() : "");
        comboEspecie.setSelectedItem(animal.geEspecie());
        comboPorte.setSelectedItem(animal.getPorte());
        comboSexo.setSelectedItem("Macho");
        comboStatus.setSelectedItem(animal.getAdotado() ? "Adotado" : "Disponível");
    }

    // ───────────────── CABEÇALHO ─────────────────
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(AMARELO);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Editar Animal");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Altere os dados do animal");
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

    // ───────────────── FORMULÁRIO ─────────────────
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        campoNome = criarCampoTexto();
        comboEspecie = criarCombo(new String[]{
                "Cachorro",
                "Gato",
                "Coelho",
                "Outro"
        });
        comboPorte = criarCombo(new String[]{
                "Pequeno",
                "Médio",
                "Grande"
        });
        comboSexo = criarCombo(new String[]{
                "Macho",
                "Fêmea"
        });
        campoRaca = criarCampoTexto();
        comboStatus = criarCombo(new String[]{
                "Disponível",
                "Adotado"
        });

        form.add(criarGrupo("Nome do animal *", campoNome));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Espécie *", comboEspecie));
        form.add(Box.createVerticalStrut(12));
        form.add(criarPainelFoto());
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Porte *", comboPorte));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Sexo", comboSexo));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Raça", campoRaca));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupo("Status *", comboStatus));

        return form;
    }

    // ───────────────── CAMPO TEXTO ─────────────────
    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField() {
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
        return campo;
    }

    // ───────────────── COMBO ─────────────────
    private JComboBox<String> criarCombo(String[] opcoes) {
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(BRANCO);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return combo;
    }

    // --------- criar foto ------------

    private JPanel criarPainelFoto() {

    JPanel grupo = new JPanel();
    grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
    grupo.setOpaque(false);

    JLabel titulo = new JLabel("Foto do Animal");
    titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));

    lblFoto = new JLabel("Nenhuma imagem");
    lblFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
    lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
    lblFoto.setPreferredSize(new Dimension(120, 120));
    lblFoto.setMaximumSize(new Dimension(120, 120));
    lblFoto.setBorder(new LineBorder(CINZA_BORDA));

    JButton btnImagem = new JButton("Trocar Imagem");

    btnImagem.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Imagens (*.jpg, *.jpeg, *.png)",
                "jpg",
                "jpeg",
                "png"
            )
        );

        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            try {

                File arquivoOriginal =
                    chooser.getSelectedFile();

                String nomeArquivo =
                    arquivoOriginal.getName();

                Path destino =
                    Path.of("imagens", nomeArquivo);

                Files.copy(
                    arquivoOriginal.toPath(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
                );

                caminhoImagem = destino.toString();

                ImageIcon icon =
                    new ImageIcon(caminhoImagem);

                Image imagem =
                    icon.getImage().getScaledInstance(
                        120,
                        120,
                        Image.SCALE_SMOOTH
                    );

                lblFoto.setText("");
                lblFoto.setIcon(new ImageIcon(imagem));

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                    this,
                    "Erro ao copiar imagem!"
                );
            }
        }
    });

    grupo.add(titulo);
    grupo.add(Box.createVerticalStrut(5));
    grupo.add(lblFoto);
    grupo.add(Box.createVerticalStrut(8));
    grupo.add(btnImagem);

    return grupo;
}

    // ───────────────── GRUPO ─────────────────
    private JPanel criarGrupo(String titulo, JComponent componente) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(componente);
        return grupo;
    }

    // ───────────────── BOTÕES ─────────────────
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 16)
        );
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
        btnCancelar.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
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
        btnSalvar.setBorder(
                new EmptyBorder(10, 24, 10, 24)
        );
        btnSalvar.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        btnSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String especie = comboEspecie.getSelectedItem().toString();
            String porte = comboPorte.getSelectedItem().toString();
            String raca = campoRaca.getText().trim();
            String status = comboStatus.getSelectedItem().toString();

            animal.setNome(nome);
            animal.setEspecie(especie);
            animal.setPorte(porte);
            animal.setRaca(raca);
            animal.setCaminhoImagem(caminhoImagem);
            animal.setaAotado("Adotado".equals(status));

            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(
                    this,
                    "Alterações salvas com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
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